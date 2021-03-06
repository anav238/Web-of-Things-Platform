import React, {useState} from 'react'
import { styled } from '@mui/material/styles';
import Button from '@mui/material/Button';
import {Add, Delete, Edit} from '@mui/icons-material';
import Tooltip from '@mui/material/Tooltip';
import Typography from '@mui/material/Typography';
import ActionDialog from './ActionDialog';
import AddDeviceDialog from './AddDeviceDialog';
import { useAuth } from "../../contexts/authcontext";
import axios from 'axios';

const ColorOutlinedButton = styled(Button)(({ theme }) => ({
    padding: '10px 30px',
    fontWeight: 600,
    color: 'rgba(183, 129, 3)',
    border: '2px solid',
    borderColor: 'rgba(183, 129, 3, 0.1)',
    backgroundColor: 'rgba(255, 247, 205)',
    '&:hover': {
        backgroundColor: 'rgba(255, 247, 205,0.9)',
        borderColor: 'rgba(183, 129, 3, 0.2)',
    },
    }));
        

const RedColorOutlinedButton = styled(Button)(({ theme }) => ({
    width:'100%',
    padding: '10px 22px',
    marginBottom:'10px',
    fontWeight: 600,
    color: 'rgba(122, 12, 46)',
    border: '2px solid',
    borderColor: 'rgba(122, 12, 46, 0.1)',
    backgroundColor: 'rgba(255, 231, 217)',
    '&:hover': {
        backgroundColor: 'rgba(255, 231, 217, 0.9)',
        borderColor: 'rgba(122, 12, 46, 0.2)',
    }
}));


export default function Devices({devices, setDevices, houseSelected, setHouseSelected}) {
    const { currentUser } = useAuth();
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const [isDialogOpen2, setIsDialogOpen2] = useState(false)
    const [actionDialog, setActionDialog] = useState({});
    const [deviceIdDialog, setDeviceIdDialog] = useState(null);

    
    const doAction = (deviceId, action) => {
        setActionDialog(JSON.parse(JSON.stringify(action)))
        setDeviceIdDialog(deviceId)
        setIsDialogOpen(true);
    }

    const deleteDevice = (deviceId) =>{
        

        const newDeviceIds = JSON.parse(JSON.stringify(houseSelected.deviceIds));
        const index = newDeviceIds.findIndex( id => id === deviceId)
        if (index !== -1) {
            newDeviceIds.splice(index, 1);
        }
        
        const payload = [
            {
                op: "add",
                path: "/deviceIds",
                value: newDeviceIds
            }
        ];


        axios.patch(`/homes/${houseSelected.id}`, payload,
                { headers: {'Content-Type': 'application/json-patch+json'} }
            )
            .then(response => { 
                setHouseSelected(response.data);
            })
            .catch(error => {
                console.log(error);
            });
    }

    const addDevice = () => {
        setIsDialogOpen2(true);
    }

    return (
        <div className='component'>
            <div className='component-header'>
            {
                (!houseSelected || houseSelected.users.find( user => user.userId===currentUser?.id)?.userRole === 'OWNER')
                    &&
                <div className='component-header-addBtn'>
                    <ColorOutlinedButton 
                        size="large"
                        startIcon={<Add />}
                        onClick={addDevice}>
                        Add device
                    </ColorOutlinedButton>
                </div>
            }
            </div>
            {
                devices.map(device => (
                <div className='component-device' key={device.id}>
                    <div className='component-device-header'>
                        <div className='component-device-header-title'>
                            {device.title}
                        </div>
                        <div className='component-device-header-subtitle'>
                            {device.description}
                        </div>
                        <div className='component-device-header-subtitle'>
                            {device.category}
                        </div>
                    </div>
                    <div className='component-device-properties'>
                    {
                        device.properties.map( (prop, index) => (
                            <Tooltip 
                                title={
                                    <Typography fontSize={18}>
                                        {prop.description}
                                    </Typography>}
                                arrow
                                key={index}
                            >
                                <div className='component-device-properties-prop'>
                                    <span className='component-device-properties-prop-title'>
                                        {prop.name}
                                    </span>
                                    : 
                                    <span className='component-device-properties-prop-value'>
                                    {prop.currentValue} {prop.unit==='percent' ? '%' : prop.unit}
                                    </span>
                                </div>
                            </Tooltip>
                        ))
                    }
                    </div>
                    <div className='component-device-actions'>
                    {
                        device.actions.map( (action, index) => (
                            <div className='component-device-actions-action'
                                key={index}
                            >
                                    <Tooltip 
                                        title={
                                            <Typography fontSize={18}>
                                                {action.description}
                                            </Typography>}
                                        arrow
                                    >
                                        <RedColorOutlinedButton size="large"
                                            onClick={() => doAction(device.id, action)}
                                            endIcon={
                                                (action.input.properties.length === 1 &&
                                                    action.input.properties[0].type === 'boolean') ?
                                                <></> : <Edit />
                                            }>
                                            {action.name}
                                        </RedColorOutlinedButton>
                                    </Tooltip>
                            </div>
                        ))
                    }
                    <div className='component-device-actions-action'
                            >
                        <Tooltip 
                            title={
                                <Typography fontSize={18}>
                                    Remove this device
                                </Typography>}
                            arrow
                        >
                            <Button size="large"
                                id="deleteDeviceBtn"
                                onClick={() => deleteDevice(device.id)}
                                startIcon={<Delete />}
                                color="error">
                                Delete device
                            </Button>
                        </Tooltip>
                    </div>
                    </div>
                </div>
                ))
            }
        
            <ActionDialog
                isOpen={isDialogOpen}
                setIsOpen={setIsDialogOpen}
                deviceId={deviceIdDialog}
                action={actionDialog}
                setAction={setActionDialog}
            />

            <AddDeviceDialog
                isOpen={isDialogOpen2}
                setIsOpen={setIsDialogOpen2}
                devices={devices}
                setDevices={setDevices}
                houseSelected={houseSelected}
                setHouseSelected={setHouseSelected}
            />
        </div>
    )
}
