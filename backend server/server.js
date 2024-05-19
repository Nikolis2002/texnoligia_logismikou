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

app.post("/getFunctionWithParams",async (req,res)=>{
    try{
        const param=req.body;
        jsonObj=JSON.parse(data);
        
        jsonMap=helper.getPostParamsJson(jsonObj);
        jsonArray=Array.from(jsonMap.values());
        let query=`CALL ${param}(?)`;

        let response= await helper.queryPromise(con,query,jsonMap);
        console.log(response.result[0][0].result);
        res.status(200).send(response.result);
    }
    catch(err){
        console.log("here2");
        res.status(500).send(new helper.ResponseMessage("Could not retrieve table").string());
    }
});

app.post("/check_user",async (req,res)=>{
    try{
        const data=req.body;
        console.log(data);
        jsonObj=JSON.parse(data);

        let queryUser= "CALL checkCustomer(?,?)";
        let queryDriver= "CALL checkDriver(?,?)";

        console.log(`${jsonObj.username} and ${jsonObj.password}`);
        let tableUser=await helper.queryPromise(con,queryUser,[jsonObj.username,jsonObj.password]);
        let tableDriver=await helper.queryPromise(con,queryDriver,[jsonObj.username,jsonObj.password]);

        if(tableUser.result[0][0].result==0 && tableDriver.result[0][0].result==0 ){
            console.log(tableUser.result);
            console.log("here dista");
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

const ip_adress=jsonPass.ip;
const port=3000;
app.listen(port, () => {
    console.log(`Server is listening on port ${port} and ip: ${ip_adress}`);
  });
