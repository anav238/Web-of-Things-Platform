import React, { useState } from 'react';
import './Dashboard.scss'
import BoardMenu from './BoardMenu';
import Houses from './Houses';
import {houses_mock, users_mock, devices_mock} from '../../assets/mockData2' 

const components = ['Houses', 'Users', 'Devices', 'Action'];

export default function Dashboard() {
    const [ componentSelected, setComponentSelected ] = useState(components[0]);

    const [houses, setHouses] = useState(houses_mock);

    const [ houseSelected, setHouseSelected ] = useState(null)
    // const [ deviceSelected, setDeviceSelected ] = useState(null)


    return (
        <div className='dashboard'>
            <div className='dashboard-title'>
                Hi, Welcome Back !
            </div>
            
            <BoardMenu 
                data={{houses_mock, users_mock, devices_mock}}
                houseSelected={houseSelected}
                setHouseSelected={setHouseSelected}
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
                                setHouseSelected={setHouseSelected}
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
