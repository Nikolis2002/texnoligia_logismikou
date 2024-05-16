const http = require('http');
const fs = require('fs');
const { URL } = require("url");
const mysql = require("mysql2");
const express= require("express");
const helper = require("./serverHelper.js");

//selecr user->username,password
//select pinakes
//select ta energa requests
//select trasports
//garage me autkinhta tou
//

const con = mysql.createConnection(
    {
        "host": "localhost",
        "user": "root",
        "password": 'Nikolis2002"',
        "database": "app_database",
        "port": 3306,
    }
);

const app=express();

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
        console.log("here");
        res.status(500).send(new helper.ResponseMessage("Could not retrieve table").string());
    }

    
});

const ip_adress="192.168.1.7";
const port=3000;
app.listen(port, () => {
    console.log(`Server is listening on port ${port} and ip: ${ip_adress}`);
  });
