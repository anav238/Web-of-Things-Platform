import React, { useState } from 'react';
import './Dashboard.scss'
import BoardMenu from './BoardMenu';
import Houses from './Houses';
import Users from './Users';
import Devices from './Devices';
import {houses_mock, devices_mock} from '../../assets/mockData2' 

const components = ['Houses', 'Users', 'Devices', 'Action'];

export default function Dashboard() {
    const [ componentSelected, setComponentSelected ] = useState(components[0]);

    const [houses, setHouses] = useState(houses_mock);
    const [devices, setDevices] = useState(devices_mock);

    const [ houseSelected, setHouseSelected ] = useState(null)
    // const [ deviceSelected, setDeviceSelected ] = useState(null)


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
