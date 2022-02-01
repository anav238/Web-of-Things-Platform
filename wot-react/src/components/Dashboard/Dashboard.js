import React, { useState, useEffect } from 'react';
import './Dashboard.scss'
import BoardMenu from './BoardMenu';
import Houses from './Houses';
import Users from './Users';
import Devices from './Devices';
import {devices_mock} from '../../assets/mockData2'
import { useAuth } from "../../contexts/authcontext";
import axios from 'axios'

const components = ['Houses', 'Users', 'Devices', 'Action'];

export default function Dashboard() {
    const { currentUser } = useAuth();
    const [ componentSelected, setComponentSelected ] = useState(components[0]);

    const [houses, setHouses] = useState([]);
    const [devices, setDevices] = useState([]);

    const [ houseSelected, setHouseSelected ] = useState(null)

    useEffect(() => {
       
        const fetchHomes = async () =>{
            await axios.get(`/users/${currentUser.id}/homes`)
            .then(response => {
                setHouses(response.data)
            })
            .catch(error => {
                console.log(error?.response);
            });
        }

        fetchHomes();
    }, [currentUser.id])

    return (
        <div className='dashboard'>
            <div className='dashboard-title'>
                Hi, Welcome Back !
            </div>
            
            <BoardMenu 
                houses={houses}
                devices={devices}
                houseSelected={houseSelected}
                components={components}
                componentSelected={componentSelected}
                setComponentSelected={setComponentSelected}
                className='dashboard-boardMenu'
            />

            <div className='dashboard-content'>
            {
                (() => {
                    switch (componentSelected) {
                        case components[0]:
                            return (
                            <Houses 
                                houses={houses}
                                setHouses={setHouses}
                                houseSelected={houseSelected}
                                setHouseSelected={setHouseSelected}
                                />
                            )

                        case components[1]:
                            return (
                            <Users
                                houseSelected={houseSelected}
                                />
                            )
                        case components[2]:
                            return (
                            <Devices
                                houseSelected={houseSelected}
                                devices={devices_mock}
                                />
                            )
                        case components[3]:
                            return (
                            <Devices
                                houseSelected={houseSelected}
                                devices={devices_mock}
                                />
                            )
                        default:
                            return (
                            <div>{componentSelected}</div>
                            )
                    }
                })()
            }
            </div>
        </div>
    )
}
