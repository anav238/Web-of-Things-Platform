import React,{useState} from 'react'
import axios from 'axios'
import TextField from '@mui/material/TextField';
import MenuItem from '@mui/material/MenuItem';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Button from '@mui/material/Button';


const deviceCategories = ['ENTERTAINMENT', 'ENVIRONMENT', 'SECURITY']

export default function AddDeviceDialog({isOpen, setIsOpen, devices, setDevices, houseSelected, setHouseSelected}) {

    const [deviceUrl, setDeviceUrl] = useState('')
    const [deviceCategory, setDeviceCategory] = useState('ENTERTAINMENT')


    const patchDeviceIds = ( newDeviceId ) => {

        const payload = [
            {
                op: "add",
                path: "/deviceIds",
                value: [...houseSelected.deviceIds, newDeviceId]
            }
        ];

        axios.patch(`/homes/${houseSelected.id}`,payload,{
            headers: {
                'Content-Type': 'application/json-patch+json',
            }
        })
        .then(response => { 
            setHouseSelected(response.data);
            console.log(response.data);
        })
        .catch(error => {
            console.log(error);
        });
    }

    const submitForm = () => {
        const payload = {
            deviceUrl: deviceUrl, 
            deviceCategory: deviceCategory
        }

        axios.post(`/devices`, payload)
            .then(response => { 
                const newDevice = response.data
                console.log(newDevice);
                patchDeviceIds(newDevice.id)
                setDevices([...devices, newDevice])
                setIsOpen(false)
            })
            .catch(error => {
                console.log(error);
            });

    }

    return (
        <>
        <Dialog
        fullWidth={true}
        maxWidth={'sm'}
        open={isOpen} 
        onClose={() => setIsOpen(false)}
        className="component-dialog"
    >
        <DialogTitle>Add a new device</DialogTitle>
        <DialogContent>
            <div className='component-dialog-homeName'>
                <DialogContentText>
                    Please fill the form below:
                </DialogContentText>
                <TextField
                    autoFocus
                    id="deviceUrl"
                    label="Device Url"    
                    type="text"
                    variant="outlined"
                    value={deviceUrl}
                    onChange={(event) => { setDeviceUrl(event.target.value)}}
                />
                <TextField
                    select
                    id="category"
                    label="Category"    
                    variant="outlined"
                    value={deviceCategory}
                    onChange={(event) => { setDeviceCategory(event.target.value)}}
                >
                    {deviceCategories.map((option) => (
                        <MenuItem key={option} value={option}>
                            {option}
                        </MenuItem>
                    ))}
                </TextField>
            </div>
        </DialogContent>
        <DialogActions>
            <Button onClick={() => setIsOpen(false)}>Close</Button>
            <Button 
                disabled={!deviceUrl}
                onClick={submitForm}>
                Add
            </Button>
        </DialogActions>
    </Dialog>
    </>
    )
}
