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

        res.status(200).send(tableData);
        
    }
    catch(err){
        console.log("here2");
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
            const fields = Object.keys(row).join(', ');
            const placeholders = Object.values(row).map(() => '?').join(', ');
            const sql = `INSERT INTO ${table} (${fields}) VALUES (${placeholders})`;
            const valuesArray = Object.values(row);

            promises.push(helper.queryPromise(con, sql, valuesArray));
        }

        const results=await Promise.all(promises);
        const insertIds = results.map(result => {
            if (result && result.result && result.result.insertId !== undefined) {
                return result.result.insertId;
            } else {
                console.error("Unexpected result format:", result);
                return null;
            }
        });
        res.status(200).json({ insertIds });
    } catch (error) {
        console.error(error);
        res.status(500).send(new helper.ResponseMessage("Could not retrieve table").string());
    }
});


app.post("/getFunctionWithParams",async (req,res)=>{
    try{
        const param=req.body;
        let jsonObj=JSON.parse(param);
        
        jsonMap=helper.getPostParamsJson(jsonObj);
        jsonArray=Array.from(jsonMap.values());
        let query=`CALL ${param}(?)`;

        let response= await helper.queryPromise(con,query,jsonArray);
        res.status(200).send(response.result);
    }
    catch(err){
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


app.post("add_card"),async(req,res)=>{
    const data=req.body;
    jsonObj=JSON.parse(data);
    let obj="CALL insertCard(?,?,?,?)";
    let query= await helper.queryPromise(con,obj,[jsonObj.cardNum,jsonObj.expDate,jsonObj.owner,jsonObj.ccv]);
    
}

const ip_adress=jsonPass.ip;
const port=3000;
app.listen(port, () => {
    console.log(`Server is listening on port ${port} and ip: ${ip_adress}`);
  });
