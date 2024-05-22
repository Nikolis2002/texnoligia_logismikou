const http = require("http");
const { resolve } = require("path");
const fs = require('fs');
const bcrypt = require('bcryptjs');
const { Console } = require("console");
const cr=require('crypto');
const saltRounds = 10;

const cookieExpirationTime = 60*60*60*6; //seconds,the time that it takes for a cookie to expire
const cookieCheckTime = cookieExpirationTime*1000;//msec, the cookie checker timer

//Classes
//==========================================================================
//classes in order to print correct error messages,and pass the error i a formatted way
class ResponseMessage
{
    constructor(msg, jsonData)
    {
        this.msg = msg;
        this.jsonData = jsonData;
    }

    string()
    {
        if (this.jsonData)
            return `{"msg": "${this.msg}", "data": ${JSON.stringify(this.jsonData)}}`;
        else
            return `{"msg": "${this.msg}"}`;
    }
}

class SQLDuplicateError
{
    constructor(error)
    {
        let duplicateRegex = /Duplicate entry '(.*?)' for key '(.*?)\.(.*?)'/;
        let res = error.sqlMessage.match(duplicateRegex);

        this.msg = res[0];
        this.value = res[1];
        this.table = res[2];
        this.field = res[3];
    }
}

//==================================== GET RELATED FUNCTIONS
async function getBaseCoords()
{
    return new Promise((resolve, reject)=>
    {
        fs.readFile("base_coords.json", "utf8", (err, data)=>
        {
            if (err)
                return reject(err);

            resolve(JSON.parse(data));
        });
    })
}

async function setBaseCoords(lat, lng)
{
    fs.writeFileSync("base_coords.json", JSON.stringify({lat: lat, lng: lng}), "utf8");
}

//reads info of body
async function readBody(msg) {
    return new Promise((resolve, reject) => {
        let body = "";
        msg.setEncoding('utf8');

        msg.on("data", (chunk) => {
            body += chunk;
        })

        msg.on("end", () => {
            resolve(body)
        })
    })
}
//get parameteers 
async function getPostParams(req) {
    let body = await readBody(req);
    
    return new Promise((resolve, reject) => {
        const params=new URLSearchParams(body);
        resolve(params);
    })
}
//returns promise of queryString through mysql database
async function queryPromise(con, queryString, values) {
    return new Promise((resolve, reject) => {
        con.query(queryString, values, (err, result, fields) => {
            if (err)
                reject(err);

            resolve({ result, fields });
        })
    })
}

async function connectPromise(con) {
    return new Promise((resolve, reject) => {
        if (con.state === "disconnected") {
            con.connect((err) => {
                if (err) {
                    reject(err);
                }

                resolve(1);
            })
        }

        resolve(1);
    })
}
//loads json using path of base
async function loadJsonFromServer()
{
    let options = 
    {
        host: "usidas.ceid.upatras.gr",
        path: "/web/2023/export.php",
        headers:
        {
            "accept": "application/json"
        }
    }

    let response = await new Promise((resolve, reject) =>
    {
        http.get(options, (res)=>
        {
            resolve(res);
        });
    })

    let body = await readBody(response);

    return JSON.parse(body);
}

async function jsonFromReq(req)
{
    let body = await readBody(req);
    return JSON.parse(body);
}
//returns as json the data of the mysql table
async function getTableData(con, table)
{
    let obj = await queryPromise(con, "SELECT * FROM " + table);
    return JSON.stringify(obj);
}

async function getJsonFromQuery(con, queryString, values)
{
    let obj = await queryPromise(con, queryString, values);
    console.log(obj)
    return JSON.stringify(obj);
}

//=======================================cookies ,functions that either get,validate or set cookies
async function userType(con, username)
{
    let obj = await queryPromise(con, "CALL personIdentifier(?)", [username]);

    return obj.result[0][0].type;
}

async function validCookie(con, sessionId)
{
    try
    {
        let obj1 = await queryPromise(con, "SELECT * FROM cookie WHERE session_id = ?", [sessionId]);
        
        let cookieExists = (obj1.result.length !== 0);

        let cookieExpired = 0;

        if (cookieExists)
        {
            let obj2 = await queryPromise(con, "SELECT * FROM cookie WHERE session_id = ? AND expiration_timestamp <= NOW()", [sessionId]);

            cookieExpired = (obj2.result.length === 1);
        }

        return (cookieExists && !cookieExpired);
    }
    catch(err)
    {
        console.error(err);
    }
}

async function setCookie(con,sessionId, username)
{
    try
    { //based on the existance if the user in the database it either updates or inserts a new session number
        let obj = await queryPromise(con,"SELECT * FROM cookie WHERE username = ?", [username]);

        if (obj.result.length === 1)
        {
            await queryPromise(con, `UPDATE cookie SET session_id = ?, expiration_timestamp = NOW() + INTERVAL ${cookieExpirationTime} SECOND WHERE username = ?`, [sessionId, username])
        }
        else if (obj.result.length === 0)
        {
            await queryPromise(con, `INSERT INTO cookie(username, session_id, expiration_timestamp) VALUES (?, ?, NOW() + INTERVAL ${cookieExpirationTime} SECOND)`, [username, sessionId])
        }
    }
    catch(err)
    {
        console.log(err);
    }
}

//session ID  getter
async function userFromSessionId(con, sessionId)
{
    try
    {
        let obj = await queryPromise(con, "SELECT username FROM cookie WHERE session_id = ?", [sessionId]);
        
        return obj.result[0].username;
    }
    catch(err)
    {
        console.error(err);
    }
}


//parses the cookie and returns an object,its used in order to parse the cookie and validate users
function cookieParser(cookieString){
    //splits the string when it finds the ; character, then splits each part of the splitted strings in the = character
    //and then puts the first half as the name of the field and the second as the field
    //the object contains all the splits done this way
    let cookieObj={};
    cookieString&&cookieString.split(';').forEach(cookie =>
    {
        const fixer=cookie.split('=');
        cookieObj[fixer.shift().trim()]=decodeURI(fixer.join('='));
    }); 
   
    return cookieObj;
}

//used to delete the cookies when session expires
async function burnCookie(con,sessionId)
{
    try{
    
     let {result,fields}=await queryPromise(con,"DELETE FROM cookie WHERE session_id = ?",[sessionId]);
     return result;
    }
    catch(err)
    {
        console.error(err);
    }
}

//=======================================cookies

//=======================================POST RELATED FUNCTIONS         
async function getPostParamsJson(req){

    const json=JSON.parse(req.body);
    jsonMap=objectMapped(json); // returns  the posted information as a json

    return jsonMap;
}

function objectMapped(obj){
    const map=new Map();
    
    for(const index of Object.keys(obj)){
        map.set(index,obj[index]); //makes a json to map
    }

    return map;
}
//creates a unique id for the logged in user
function generateUniqueId()
{
    return Math.floor(Math.random() * 100000);
}


async function userFromSessionId(con, sessionId)
{
    try
    {
        let obj = await queryPromise(con, "SELECT username FROM cookie WHERE session_id = ?", [sessionId]);
        return obj.result[0].username;
    }
    catch(err)
    {
        console.log(err);
    }
}

//used for password verification
async function verifier(password,hash){
    return await bcrypt.compare(password,hash);
}
async function searchUser(con, data) {

    const user = data.get('username');
    let password = data.get('password');

//checks for the user and then for the password and if something goes wrong it prints the correct message
    try
    {
        let obj1 = await queryPromise(con, "SELECT * FROM person WHERE username = ?", [user]);
        let userExists = (obj1.result.length === 1);

        let rightPassword = false;

        if (userExists)
        {
            let obj2 = await queryPromise(con, "SELECT password FROM person WHERE username = ?", [user]);
            rightPassword = await verifier(password,obj2.result[0].password);
        }

        if (!userExists)
        {
            console.log(`User "${user}" does not exist`);
            return {success: false, msg: `User "${user}" does not exist`};
        }
        else if (userExists && !rightPassword)
        {
            console.log("Wrong password");
            return {success: false, msg: "Wrong password"};
        }
        else
        {
            console.log("Success");
            return {success: true, msg: ""};
        }
    }
    catch(err)
    {
        console.error(err);
    }
}

//function that hashes password with the help of bcryptjs
async function hashPassword(password){
    try{
        const salt=await bcrypt.genSalt(saltRounds);
        const hash=await bcrypt.hash(password,salt);

        return hash;

    }
    catch(error){
        console.error("Error when trying to hash the password:",error);
    }
}

async function insertRequest(con,data){
    try{
        let insertReq= await getPostParamsJson(data[0]);
        insertReq=Array.from(insertReq.values());
        //puts the data in the front of the array in order to sore them in the database
        insertReq.unshift(data[1]);
        
        queryString="CALL insertRequest(?)"
        let { result, fields } = await queryPromise(con,queryString,[insertReq]);
        
        return;

    }catch(err){console.error(err);}
}

async function registerQuery(con,req,type)
{
    try
    {
        let jsonData = await jsonFromReq(req);
        //inserts the user based on his type as as a user first and then in the correct category
        //that is being done with a transaction so in case one of the inserts goes wrong,nothing will be stored in the database
        let hashedPassword= await hashPassword(jsonData.password);
        return new Promise((resolve, reject)=>
        {
            con.beginTransaction(async (err) =>
            {
                if (err)
                    return reject(err);
                try
                {
                    if(type==="rescuer")
                    {
                        let coords = await getBaseCoords();

                        let obj = await queryPromise(con, 'CALL insertRescuer(?, ?, POINT(?, ?))', [jsonData.username, hashedPassword, coords.lat, coords.lng]);
                    }
                    else if(type==="citizen")
                    {
                        //console.log("I remember you're, CITIZENS");
                        let obj=await queryPromise(con,'CALL insertCitizen(?, ?, ?, ?, ?)',[jsonData.username, hashedPassword, jsonData.fullName, jsonData.phoneNumber, jsonData.family]);
                    }
                        
                }
                catch(queryError)
                {
                    console.log(queryError);
                    return con.rollback(() => {reject(queryError);});
                }

                con.commit((commitErr)=>
                {
                    if (commitErr)
                        return con.rollback(() => {reject(commitErr)});
                })

                resolve(jsonData.username);
            })
        })
    }
    catch(err)
    {
        console.error(err);
        return Promise.reject();
    }
}

async function insertAnnouncement(con,req)
{
    try{
    
        let insertAnn= await getPostParamsJson(req);
        insertAnn=Array.from(insertAnn.values());

        //same logic as the function above
        return new Promise((resolve, reject)=>{
            con.beginTransaction(async (err)=>
            {
                
                if(err)
                    reject(err);
                
                let queryString="INSERT INTO announcement(id) VALUES(?)"

                let {result,fields}=await queryPromise(con,queryString,[null]);

                const announcement_id=result.insertId;
                

                queryString="INSERT INTO announcement_item VALUES (?, ?)";

                //Do not allow empty announcements
                if (insertAnn.length === 0)
                {
                    console.log("Empty announcement not allowed")
                    return con.rollback(() => {reject("Empty announcement not allowed")});
                }

                insertAnn.forEach(async function(element, index)
                {
                    try
                    {
                        let obj = await queryPromise(con,queryString,[announcement_id,element[1]]);
                    }
                    catch(queryError)
                    {
                        return con.rollback(() => {reject(queryError);});
                    }

                    if(index===insertAnn.length-1){

                        con.commit((commitErr)=>{
                            if (commitErr)
                                return con.rollback(() => {reject(commitErr)});
                            });

                        resolve();
                        }
                    });

            });
        });
    }catch(err)
    {
        console.error(err);
        return Promise.reject();
    }

}

async function serveFile(fileList, request, serverResponse, extension)
{
    //Header object
    let head = new Object();

    //Headers
    let contentType;
    let cacheControl;

    switch(extension)
    {
        case "html":
        case "htm":
        {
            contentType = "text/html";
            cacheControl = "public, no-cache"
        }
        break;

        case "js":
        {
            contentType = "text/javascript";
            cacheControl = "public, max-age=15778463" //6 months
        }
        break;

        case "css":
        {
            contentType = "text/css";
            cacheControl = "public, max-age=15778463" //6 months
        }
        break;

        case "png":
        {
            contentType = "image/png";
            cacheControl = "public, max-age=15778463" //6 months
        }
        break;
    }

    if (contentType)
    {
        head["Content-Type"] =  contentType;
        head["Cache-Control"] = cacheControl;
    }

    //Read all the separate files asynchronously
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    let promiseArray = [];

    for (let f of fileList)
        promiseArray.push(fs.promises.readFile(f));
        
    //Wait for ALL the files to be read and then send their data
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    Promise.all(promiseArray).then((data)=>
    {
        if (extension === "html")
        {
            let status = 200;

            let fileContents = data[0];
            let etag = Etag(fileContents);

            head["Etag"] = etag;

            let clientEtag = request.headers['if-none-match'];

            //console.log(clientEtag);

            if (!clientEtag) //Client did not have a cached version, send file for the first time
            {
                //console.log(`Sending file ${fileList[0]} for the first time`);
            }
            else
            {
                if (clientEtag !== etag) //Client had cached version, but it's old
                {
                    //console.log(`File ${fileList[0]} changed, sending new version`);
                }
                else //Nothing changed
                {
                    status = 304 //Not modified
                }
            }

            serverResponse.writeHead(status, headers = head);

            if (status === 200)
                serverResponse.write(fileContents);
        }
        else
        {
            serverResponse.writeHead(200, headers = head);

            for (let d of data)
            {
                serverResponse.write(d);

                if (extension !== "png")
                    serverResponse.write("\n");
            }
        }

        serverResponse.end();
    })
    .catch((err)=>
    {
        console.error(err);
    })
}

function Etag(value)
{
    let hash=cr.createHash('sha1');
    hash.update(value);

    return `"${hash.digest('hex')}"`;
}

async function jsonToDatabase(con, jsonObject)
{
    let categories = jsonObject.categories;
    let items = jsonObject.items;

    let badCat = [];

    //Categories
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    const idiotDetector = /(.*[Hh]acker.*)|(.*[Tt]est.*)|(^$)|(.*[-]{2,}.*)/

    categories = categories
    .filter(category =>
    {
        let isBadCategory = category.category_name.match(idiotDetector)

        if (isBadCategory)
            badCat.push(category.id);

        return !isBadCategory;
    })
    .map(category => [category.id, category.category_name]);

    //Items
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    items = items.filter(item =>
    {
        let isGood = ((item.name !== "") && !(badCat.includes(item.category)));

        return isGood;
    });

    let details = [...items];

    items = items.map(item => [item.id, item.category, item.name]);

    //Details
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //After the first map, for each item, we get an array like this:
    //  [[id1, det_name_1, det_val_1], [id1, det_name_2, det_val_2], ...]
    //In the end, we have an array of arrays, of arrays...
    //  [
    //      [[id1, det_name_1_1, det_val_1_1], [id1, det_name_1_2, det_val_1_2], ...]
    //      [[id2, det_name_2_1, det_val_2_1], [id2, det_name_2_2, det_val_2_2], ...]
    //      ...
    //      [[idN, det_name_N_1, det_val_N_1], [id1, det_name_N_2, det_val_N_2], ...]
    //  ]
    //This is not what we want. We need to UNWRAP each item array afterwards
    //That's what the loop afterwards does
    details = details.map((item) =>
    {
        let arr = [];

        for(let detail of item.details)
        {
            if (detail.detail_name !== "")
                arr.push([item.id, detail.detail_name, detail.detail_value]);
        }

        return arr;
    })

    let newdetails = [];

    for (let item of details)
    {
        newdetails.push(...item);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    console.log(categories);
    console.log(items);
    console.log(newdetails);

    try
    {
        await queryPromise(con, "DELETE FROM category");
        await queryPromise(con, "DELETE FROM item");
        await queryPromise(con, "DELETE FROM detail");

        await queryPromise(con ,"INSERT INTO category(id, name) VALUES ?", [categories]);

        for (let item of items)
        {
            try
            {
                await queryPromise(con, "INSERT INTO item(id, category_id, name) VALUES (?, ?, ?)", [...item]);
            }
            catch(err)
            {
                if (err.code === "ER_DUP_ENTRY")
                {
                    //Ignore duplicates
                    continue;
                }
            }
        }

        for (let det of newdetails)
        {
            try
            {
                await queryPromise(con, "INSERT INTO detail(item_id, detail_name, detail_value) VALUES (?, ?, ?)", [...det]);
            }
            catch(err)
            {
                if (err.code === "ER_DUP_ENTRY")
                {
                    //Ignore duplicates
                    continue;
                }
            }
        }
    }
    catch(err)
    {
        if (err.code === "ER_DUP_ENTRY")
        {
            console.error(err);
        }
        else console.error(err);
    }
}

//===============================================================================================

function getAuthorization(user, checked, flag) {

    let commonFiles = ["/", "error.html", "style.css", "pending_offer.png", "pending_request.png", "assigned_offer.png", "assigned_request.png", "base.png", "shadow.png", "truck.png"];

    let commonEndpoints = ["/select", "/LogOut", "/getBaseLocation"];

    let adminEndPoints = ["/getStock", "/json", "/statistics", "/createRescuer", "/insertAnnouncement", "/addCategory",
    "/addItem", "/deleteItem", "/loadFromFile", "/editItem", "/setStock", "/setBaseLocation"];

    let adminFiles = ["admin.js", "admin.html", "stock.html", "stock.js", "admin2.js", "inventory.html", "inventory.js", "loadItems.js","statistics.html","statistics.js", "newCategory.js"];

    let citizenEndPoints = ["/getData", "/pastOffers", "/ongoingOffers" , "/seeRequests", "/request", "/submitOffer",
    "/cancelOffer", "/family"];

    let citizenFiles = ["citizen.html", "citizen.js", "offers.html", "offers.js"];

    let rescuerEndPoints = ["/getRescuerLocation", "/myInventory", "/rescuerPendingRequests", "/rescuerPendingOffers",
    "/loadItems", "/unloadItems", "/modifyRequest", "/completeRequest", "/updateRescuerLocation", "/modifyOffer"];

    let rescuerFiles = ["rescuer.html", "rescuer.js"];

    let files = [];
    let endPoints = [];

    switch (user) {
        case 'admin':
            files = [...adminFiles,...commonFiles];
            endPoints = [...adminEndPoints,...commonEndpoints];
            break;
        case 'citizen':
            files = [...citizenFiles,...commonFiles];
            endPoints = [...citizenEndPoints,...commonEndpoints];
            break;
        case 'rescuer':
            files = [...rescuerFiles,...commonFiles];
            endPoints = [...rescuerEndPoints,...commonEndpoints];
            break;
        default:
            return false; 
    }

    if (flag) {
        return files.includes(checked);
    } else {
        return endPoints.includes(checked);
    }
}

function checkSelectEndPoint(user,table){
    let citizenTables=["announcement_items"];
    
    let adminTables=["global_inventory","rescuer","rescuer_inventory_formatted",
    "announcement_items","all_ongoing_requests","all_ongoing_offers","detail", "items_with_category_names", "category", "stock_breakdown"];

    let rescuerTables=["base_inventory_with_item_names"];

    let allowedTables= [];

    switch (user) {
        case 'admin':
            allowedTables=adminTables;
            break;
        case 'citizen':
            allowedTables=citizenTables;
            break;
        case 'rescuer':
            allowedTables=rescuerTables;
            break;
        default:
            return false; 
    }

    return allowedTables.includes(table);
}




module.exports = {
    readBody,
    getPostParams,
    searchUser,
    queryPromise,
    connectPromise,
    registerQuery,
    loadJsonFromServer,
    getTableData,
    getJsonFromQuery,
    setCookie,
    generateUniqueId,
    cookieParser,
    burnCookie,
    getPostParamsJson,
    validCookie,
    userFromSessionId,
    insertRequest,
    cookieExpirationTime,
    cookieCheckTime,
    serveFile,
    ResponseMessage,
    userType,
    SQLDuplicateError,
    insertAnnouncement,
    jsonToDatabase,
    getBaseCoords,
    setBaseCoords,
    jsonFromReq,
    getAuthorization,
    checkSelectEndPoint,
    hashPassword,
    Etag
};
