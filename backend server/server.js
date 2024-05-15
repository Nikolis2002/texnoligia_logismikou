const http = require('http');
const fs = require('fs');
const { URL } = require("url");
const mysql = require("mysql2");
const express= require("express");

//selecr user->username,password
//select pinakes
//select ta energa requests
//select trasports
//garage me autkinhta tou
//

const con = mysql.createConnection(
    {
        "host": "localhost",
        "user": "server_user",
        "password": 'password',
        "database": "web_project_2023"
    }
);

const app=express();

app.get("/test",(req,res)=>{
    res.status(200).send("hello dista");
});

app.get("/getTableData",async (req,res)=>{
    try{
        const param=eq.query.param;
        let queryString=`SELECT * FROM ${param}`;
        let tableData=await queryPromise(con, queryString, values);

        res.status(200).send(tableData);
        
    }
    catch(err){
        res.status(500).send(new helper.ResponseMessage("Could not retrieve table").string());
    }

    
});

const ip_adress="192.168.35.170";
const port=3000;
app.listen(port, () => {
    console.log(`Server is listening on port ${port} and ip: ${ip_adress}`);
  });


  async function queryPromise(con, queryString, values) {
    return new Promise((resolve, reject) => {
        con.query(queryString, values, (err, result, fields) => {
            if (err)
                reject(err);

            resolve({ result, fields });
        })
    })
}

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