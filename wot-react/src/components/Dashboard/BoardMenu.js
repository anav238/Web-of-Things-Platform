import React from 'react'
import {MapsHomeWork, Group, EmojiObjects, PlayLesson, } from '@mui/icons-material';


export default function BoardMenu(
    {
        houses, devices,
        houseSelected,
        setHouseSelected,
        components,
        componentSelected,
        setComponentSelected
    }
    ) 
{

    return (
        <div className='dashboard-boardMenu'>
            <div className='dashboard-boardMenu-row'>
                <div className='dashboard-boardMenu-row-item green'
                    onClick={() => {setComponentSelected(components[0]); setHouseSelected(null)}}>
                    <div className={`dashboard-boardMenu-row-item-icon green-linear 
                        ${components[0] === componentSelected ? 'green-linear--selected' : ""}`}>
                        <MapsHomeWork fontSize='large'/> 
                    </div>
                    {
                        houseSelected ? 
                        (<>
                            <div className='dashboard-boardMenu-row-item-title'>
                                {houseSelected.name} 
                            </div>
                            <div className='dashboard-boardMenu-row-item-subtitle'>
                                {houses?.length} houses 
                            </div>
                        </>)
                        :
                        (<>
                            <div className='dashboard-boardMenu-row-item-title'>
                                {houses?.length} houses 
                            </div>
                        </>)

                    }
                </div>
                <div className={`dashboard-boardMenu-row-item blue ${!houseSelected ? 'gray' : ""}`}
                    onClick={() => {
                        if(houseSelected)
                            setComponentSelected(components[1])
                    }}
                >
                    <div className={`dashboard-boardMenu-row-item-icon blue-linear ${!houseSelected ? 'gray-linear' : ""}
                        ${components[1] === componentSelected ? 'blue-linear--selected' : ""}`}>
                        <Group fontSize='large'/> 
                    </div>
                    <div className='dashboard-boardMenu-row-item-title'>
                        Users
                        {
                            houseSelected &&
                            <div className='dashboard-boardMenu-row-item-title-count'>
                                {houseSelected.users?.length}
                            </div> 
                        }
                    </div>                   
                </div>
            </div>
            <div className='dashboard-boardMenu-row'>
                <div className={`dashboard-boardMenu-row-item yellow ${!houseSelected ? 'gray' : ""}`}
                    onClick={() => {
                        if(houseSelected)
                            setComponentSelected(components[2])
                    }}
                >
                        <div className={`dashboard-boardMenu-row-item-icon yellow-linear ${!houseSelected ? 'gray-linear' : ""}
                        ${components[2] === componentSelected ? 'yellow-linear--selected' : ""}`}>
                        <EmojiObjects fontSize='large'/> 
                    </div>
                    <div className='dashboard-boardMenu-row-item-title'>
                        Devices
                        {
                            houseSelected &&
                            <div className='dashboard-boardMenu-row-item-title-count'>
                                {houseSelected.deviceIds?.length}
                            </div>
                        }  
                    </div>
                </div>
                <div className={`dashboard-boardMenu-row-item red ${!houseSelected ? 'gray' : ""}`}
                    onClick={() => {
                        if(houseSelected)
                            setComponentSelected(components[3])
                    }}
                >
                    <div className={`dashboard-boardMenu-row-item-icon red-linear ${!houseSelected ? 'gray-linear' : ""} 
                        ${components[3] === componentSelected ? 'red-linear--selected' : ""}`}>
                        <PlayLesson fontSize='large'/> 
                    </div>
                    <div className='dashboard-boardMenu-row-item-title'>
                        Action
                        {   
                            houseSelected &&
                            <div className='dashboard-boardMenu-row-item-title-count'>
                                {devices.reduce((sum, device) => sum + device.actions?.length, 0)}
                            </div> 
                        }  
                    </div>
                </div>
            </div>
        </div>
    )
}
