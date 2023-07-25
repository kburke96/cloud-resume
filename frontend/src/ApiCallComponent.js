import React, { useState, useEffect } from 'react';
import './apiCallStyles.css';

function ApiCallComponent() {  
    // Effect to fetch data from the API when the component mounts
    useEffect(() => {
      // Function to fetch the API data
        const fetchData = async () => {
            fetch('https://cloud-resume-counter-http-function.azurewebsites.net/api/HttpExample?name=kevin&code=r4VX2A9k5HUDWq0Y39dssXdb5uNepEku9IWxy25vX4uhAzFuLIBnkQ==')
                .then(response => {
                    if (!response.ok) {
                        console.log("network error!");
                    }
                    return response.text();
                })
                .then(text => {
                    console.log("text from response: " + text);
                })
                .catch(error => {
                    console.error("Unexpected error: ", error);
                })
        };
  
      fetchData();
    }, []);
  
    return (
        <div>
            {(
                <div id="bounds">
                    <h1>Kevin Burke</h1>
                    <h2>Software Engineer</h2>
                    <br></br>
                    <h2>Summary</h2>
                    <p>A Java Software Engineer passionate about cloud-computing, data streaming and DevOps practices.</p>
                    <br></br>
                    <h3>Work History</h3>
                    <p>Software Engineer - <i>General Motors, Limerick, Ireland</i></p>
                    <p>2022 - Present</p>
                    <p>Building a highly-scalable, fault tolerant system for ingesting, processing and storing telemetry data from 10million+ vehicles, using Java, Akka, Kafka, Pulsar and Reactive streaming principles.</p>
                    <br></br>
                    <p>Software Engineer - <i>ACI Worldwide, Limerick</i></p>
                    <p>2018 - 2022</p>
                    <p>Working across teams to build security applications, manage build and release pipelines and handle integrations with third-party software.</p>
                    <h3>Education</h3>
                    <p>B.Eng Electronic & Computer Engineer - <i>University of Limerick - 2018</i></p>
                    <div id="footer">
                        <h2 id="name">(c) 2023</h2>
                    </div>
                </div>
            )}
        </div>
    );
  }
  
  export default ApiCallComponent;
  