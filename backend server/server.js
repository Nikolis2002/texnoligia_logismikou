const http = require('http');
const fs = require('fs');
const { URL } = require("url");
const mysql = require("mysql2");
const express= require("express");
const helper = require("./serverHelper.js");



const data = fs.readFileSync('password.json', 'utf8');
const jsonPass = JSON.parse(data);
const usr=jsonPass.user;
const password=jsonPass.password;

const con = mysql.createConnection(
    {
        "host": "localhost",
        "user": usr,
        "password": password,
        "database": "app_database",
        "port": 3306,
    }
);

const app=express();


app.use((req, res, next) => {
    if (req.is('application/json')) {
        let data = '';
        req.setEncoding('utf8');
        req.on('data', (chunk) => {
            data += chunk;
        });
        req.on('end', () => {
            req.body = JSON.parse(data);
            next();
        });
    } else {
        next();
    }
});

app.get("/test",(req,res)=>{
    res.status(200).send("hello dista");
});

app.get("/getTableData",async (req,res)=>{
    try{

        const param = req.query.tableName;
        
        let queryString=`SELECT * FROM ${param}`;
        console.log(queryString);
        let tableData=await helper.getTableData(con,param);
        let json=JSON.parse(tableData);
        console.log(json.result);
        res.status(200).send(json.result);
        
    }
    catch(err){
        console.log("here2");
        res.status(500).send(new helper.ResponseMessage("Could not retrieve table").string());
    }

    
});

app.get("/getTracker",async (req,res)=>{
    try{
        const param=req.query.TrackerType;
        let tracker={};
        if(param==="vechicleTracker"){
            tracker.type="simple";
            tracker.coords={x:"38.2442870",y:"21.7326153"};
            tracker.isStopped="true";
            tracker.distance="5000";
        }
        else{
            tracker.type="special";
            tracker.coords={x:"38.2442870",y:"21.7326153"};
            tracker.isStopped="true";
            tracker.distance="5000";
            tracker.gas="100";
        }
        res.status(200).send(tracker);

    }
    catch(err){
        res.status(500).send(new helper.ResponseMessage("Could not coonect to tracker").string());
    }
});

app.post("/insertTaxiService", async (req,res)=>{
    try{
        const param=req.body;

        const queryString = `CALL taxiReservation(?, ?, ?, ST_GeomFromText(?), ST_GeomFromText(?))`;


        let jsonObj=JSON.parse(param);
        console.log(jsonObj);
        
        const array=jsonObj.values[0];

        let pickupCoords = JSON.parse(array.taxiReq_pickup_location);
        let pickupLat = parseFloat(pickupCoords.lat);
        let pickupLng = parseFloat(pickupCoords.lng);
        array.taxiReq_pickup_location = array.taxiReq_pickup_location = `POINT(${pickupLat} ${pickupLng})`;

        // Convert destination to ST_GeomFromText format
        let destinationCoords = JSON.parse(array.taxiReq_destination);
        let destinationLat = parseFloat(destinationCoords.lat);
        let destinationLng = parseFloat(destinationCoords.lng);
        array.taxiReq_destination = `POINT(${destinationLat} ${destinationLng})`;

        // Extract values from the array object
        const arrayValues = Object.values(array);
        console.log(array);
        console.log(arrayValues);

        // Your database query function
        let response = await helper.queryPromise(con, queryString, arrayValues);
        console.log(response.result[0][0].service_id);
        res.status(200).send(JSON.stringify(response.result[0][0].service_id));
    }
    catch(err){
        console.error(err);
        res.status(500).send(new helper.ResponseMessage("Could not retrieve table").string());
    }
});



app.post("/insertTable", async (req, res) => {
    try {
        const param = req.body;
        let jsonObj = JSON.parse(param);
        console.log(jsonObj);

        const { table, values } = jsonObj; // Extract table name and values array from request body
        const promises = [];

        for (const row of values) {
            // Prepare the fields and values for the SQL query
            const fields = [];
            const placeholders = [];
            const valuesArray = [];

            for (const [key, value] of Object.entries(row)) {
                fields.push(key);

                if (key === 'pickup_location' || key === 'destination'|| key==="coords") {
                    // Parse the JSON string to extract latitude and longitude
                    const coords = JSON.parse(value);
                    const lat = parseFloat(coords.lat);
                    const lng = parseFloat(coords.lng);
                    placeholders.push(`ST_GeomFromText('POINT(${lat} ${lng})')`);
                } else {
                    placeholders.push('?');
                    valuesArray.push(value === 'null' ? null : value);
                }
            }

            const sql = `INSERT INTO ${table} (${fields.join(', ')}) VALUES (${placeholders.join(', ')})`;

            promises.push(helper.queryPromise(con, sql, valuesArray));
        }

        const results = await Promise.all(promises);
        const insertIds = results.map(result => {
            if (result && result.result && result.result.insertId !== undefined) {
                return result.result.insertId;
            } else {
                console.error("Unexpected result format:", result);
                return null;
            }
        });
        res.status(200).send(insertIds);
    } catch (error) {
        console.error(error);
        res.status(500).send(new helper.ResponseMessage("Could not retrieve table").string());
    }
});





app.post("/getFunctionWithParams", async (req, res) => {
    try {
        const param = req.body;
        let jsonObj = JSON.parse(param);
        const { table, values } = jsonObj;
        
        let queryString = `CALL ${table}(`;
        const array = values[0];
        let queryParams = [];

        // Iterate over the keys and values of the array
        for (let [key, value] of Object.entries(array)) {
            if (key === "coords") {
                let dbCoords = JSON.parse(value);
                let dbLat = parseFloat(dbCoords.lat);
                let dbLng = parseFloat(dbCoords.lng);
                queryString += "ST_GeomFromText(POINT(?, ?)),";
                queryParams.push(dbLat, dbLng);
            } else {
                queryString += "?,";
                queryParams.push(value);
            }
        }

        // Remove the trailing comma and close the parenthesis
        queryString = queryString.slice(0, -1) + ")";
        
        // Execute the query
        let response = await helper.queryPromise(con, queryString, queryParams);
        res.status(200).send(response.result);
    } catch (err) {
        console.error(err);
        res.status(500).send(new helper.ResponseMessage("Could not retrieve table").string());
    }
});


app.post("/check_user",async (req,res)=>{
    try{
        const data=req.body;
        jsonObj=JSON.parse(data);

        let queryUser= "CALL checkCustomer(?,?)";
        let queryDriver= "CALL checkDriver(?,?)";

        console.log(`${jsonObj.username} and ${jsonObj.password}`);
        let tableUser=await helper.queryPromise(con,queryUser,[jsonObj.username,jsonObj.password]);
        let tableDriver=await helper.queryPromise(con,queryDriver,[jsonObj.username,jsonObj.password]);

        if(tableUser.result[0][0].result==0 && tableDriver.result[0][0].result==0 ){
            console.log(tableUser.result);
            res.status(500).send(new helper.ResponseMessage("User not registered").string());
        }
        else if(tableUser.result[0][0].result==0){
            console.log("here dista2");
            
            
            let coords=tableDriver.result[0][0].taxi_coords;
            delete tableDriver.result[0][0].taxi_coords;
            
            tableDriver.result[0][0].lat=coords.x;
            tableDriver.result[0][0].lng=coords.y;

            console.log(tableDriver.result);
            
            res.status(200).send(tableDriver.result);
        }
        else{
            console.log("here dista3");
            console.log(tableUser.result);
            res.status(200).send(tableUser.result);
        }
    }
    catch(err){
        console.error("Error processing request:", err);
        res.status(500).send(new helper.ResponseMessage("Could not retrieve data").string());
    }
});

app.post("/add_card"),async(req,res)=>{
    try{
    console.log("dista");
    const data=req.body;
    jsonObj=JSON.parse(data);
    let obj="CALL insertCard(?,?,?,?)";
    let query= await helper.queryPromise(con,obj,[jsonObj.cardNum,jsonObj.expDate,jsonObj.owner,jsonObj.ccv]);
    console.log(query[0][0].result);
    }
    catch(err){
        console.error("Error processing request:", err);
        res.status(500).send(new helper.ResponseMessage("Could not retrieve data").string());
    }
}

app.get("/check_reservation", async(req,res)=>
{
    try
    {   
        const data=req.body;

        let tableData = await helper.queryPromise(con, "CALL check_rental_available(?)", [req.query.vehicle]);

        console.log(`"Available: ${tableData.result[0][0].result}"`)

        res.status(200).send(`${tableData.result[0][0].result}`);
    }
    catch(err)
    {
        console.error("Error processing request:", err);
        res.status(500).send("Could not process request");
    }
});

app.post("/reserveRental", async(req,res)=>
{
    try
    {
        let data=req.body;

        console.log(`Reservation information: ${data}`)
        data=JSON.parse(data);

        let serviceInsert = await helper.queryPromise(con, "INSERT INTO service VALUES(?, NOW(), ?, 'ONGOING', ?)", [null, null, null]);
        let rentalInsert = await helper.queryPromise(con, "INSERT INTO rental_service VALUES(?, ?, ?, ?, ?, ?, ?, ?)", [serviceInsert.result.insertId, data.selected_vehicle, null, null, null, null, null, null]);

        await helper.queryPromise(con, "UPDATE rental SET free_status = 'FALSE' WHERE id = ?", [data.selected_vehicle])

        res.status(200).send({id: serviceInsert.result.insertId});
    }
    catch(err)
    {
        console.error("Error processing request:", err);
        res.status(500).send(new helper.ResponseMessage("Could not process request").string());
    }
});
    

const ip_adress=jsonPass.ip;
const port=3000;
app.listen(port, () => {
    console.log(`Server is listening on port ${port} and ip: ${ip_adress}`);
  });
