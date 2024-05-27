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
let distance=

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
        let ten= +req.query.addTen+10;

        let tracker={};
        if(param==="vechicleTracker"){
            tracker.type="simple";
            tracker.coords={x:"38.2442870",y:"21.7326153"};
            tracker.isStopped="true";
            tracker.distance=ten;
        }
        else{
            tracker.type="special";
            tracker.coords={x:"38.2442870",y:"21.7326153"};
            tracker.isStopped="true";
            tracker.distance=ten;
            tracker.gas="100";
        }
        console.log(tracker);
        res.status(200).send(tracker);

    }
    catch(err){
        res.status(500).send(new helper.ResponseMessage("Could not coonect to tracker").string());
    }
});

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

app.get("/getGarages", async(req,res)=>
{
    try
    {   
        let garageList = await helper.queryPromise(con, "SELECT * FROM garage");
        garageList = garageList.result;

        for (let garage of garageList)
        {
            let vehicleList = await helper.queryPromise(con, "SELECT type, id, manufacturer, model, manuf_year, license_plate, rate, seats, gas FROM out_city_vehicles WHERE garage_id = ?", [garage.id]);
            garage.vehicles = vehicleList.result;
        }

        res.status(200).send(garageList);
    }
    catch(err)
    {
        console.error("Error processing request:", err);
        res.status(500).send(new helper.ResponseMessage("Could not process request").string());
    }
});

app.get("/history", async(req, res)=>
{
    const user = req.query.user;

    console.log(`User: ${user}`)

    try
    {   
        let historyList = await helper.queryPromise(con, "SELECT * FROM history WHERE user = ?", [user]);
        historyList = historyList.result;

        for (let entry of historyList)
        {
            delete entry.user;
            
            //Give rating to every entry in history list, the same way for all services
            let ratingData = await helper.queryPromise(con, `SELECT * FROM ${entry.type}_rating WHERE id = ?`, [entry.rating_id]);
            delete entry.rating_id;
            entry.rating = ratingData.result[0];

            //Extra data for each service
            switch(entry.type)
            {
                case "rental":
                {
                    //Vehicle data
                    let vehicleData = await helper.queryPromise(con, "SELECT * FROM rental_vehicles WHERE id = ?", [entry.other_id]);
                    delete entry.other_id;
                    entry.vehicle = vehicleData.result[0];
                }
                break;

                case "taxi":
                {
                    //Taxi request
                    let requestData = await helper.queryPromise(con, "SELECT * FROM taxi_request WHERE id = ?", [entry.other_id]);
                    entry.taxi_request = requestData.result[0];

                    let vehicleData = await helper.queryPromise(con, "SELECT * FROM taxi_request_vehicle WHERE request_id = ?", [entry.other_id]);
                    entry.vehicle = vehicleData.result[0];

                    delete entry.vehicle.request_id;
                    delete entry.other_id;
                }
                break;

                case "out_city":
                {
                    //Vehicle data
                    let vehicleData = await helper.queryPromise(con, "SELECT * FROM out_city_vehicles WHERE id = ?", [entry.other_id]);
                    delete entry.other_id;
                    entry.vehicle = vehicleData.result[0];
                }
                break;
            }
        }

        res.status(200).send(historyList);
    }
    catch(err)
    {
        console.error("Error processing request:", err);
        res.status(500).send(new helper.ResponseMessage("Could not process request").string());
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
        console.log(queryString);
        console.log(queryParams);
        
        // Execute the query
        let response = await helper.queryPromise(con, queryString, queryParams);
        console.log(response.result)
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

app.post("/reserveRental", async(req,res)=>
{
    try
    {
        let data=req.body;

        console.log(`Reservation information: ${data}`)
        data=JSON.parse(data);

        let serviceInsert = await helper.queryPromise(con, "INSERT INTO service VALUES(?, NOW(), ?, 'ONGOING', ?, '0')", [null, null, null]);
        let rentalInsert = await helper.queryPromise(con, "INSERT INTO rental_service VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)", [serviceInsert.result.insertId, data.selected_vehicle, null, null, null, null, null, null, null]);

        await helper.queryPromise(con, "UPDATE rental SET free_status = 'FALSE' WHERE id = ?", [data.selected_vehicle])

        res.status(200).send({id: serviceInsert.result.insertId});
    }
    catch(err)
    {
        console.error("Error processing request:", err);
        res.status(500).send(new helper.ResponseMessage("Could not process request").string());
    }
});

app.post("/savePhoto", async (req, res) => {
    try {
        const param = req.body;
        let jsonObj = JSON.parse(param);
        const { table, values } = jsonObj;
        //console.log(jsonObj);
        let queryString = `CALL ${table}(`;
        const array = values[0];
        let queryParams = [];
        for (let [key, value] of Object.entries(array)) {
            if (key === "coords") {
                let dbCoords = JSON.parse(value);
                let dbLat = parseFloat(dbCoords.lat);
                let dbLng = parseFloat(dbCoords.lng);
                queryString += "ST_GeomFromText(POINT(?, ?)),";
                queryParams.push(dbLat, dbLng);
            } else if(key === "license"){
                let array=JSON.parse(value);
                //console.log(test);
                queryString += "?,"; 
                const buffer = Buffer.from(array);
                queryParams.push(buffer);
            }
            else{
                queryString += "?,";   
                queryParams.push(value);
                
        
            }
        }
        
        // Remove the trailing comma and close the parenthesis
        queryString = queryString.slice(0, -1) + ")";
        //console.log(queryString);
        //console.log(queryParams);
        
        // Execute the query
        let response = await helper.queryPromise(con, queryString, queryParams);
        console.log(response.result)
        res.status(200).send(response.result);
    } catch (err) {
        console.error(err);
        res.status(500).send(new helper.ResponseMessage("Could not retrieve table").string());
    }
});

const ip_adress=jsonPass.ip;
const port=3000;
app.listen(port, () => {
    console.log(`Server is listening on port ${port} and ip: ${ip_adress}`);
  });
