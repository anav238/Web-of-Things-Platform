import React from 'react'
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Button from '@mui/material/Button';
import axios from 'axios';


export default function ActionDialog({isOpen ,setIsOpen,deviceId, action, setAction}) {


    const submitForm = () => {
        const data = {}
        data[action.name] = { input: {}};
        action.input.properties.map( prop => (
            data[action.name].input[prop.name] = parseInt(prop.currentValue, 10)
        ))
        axios.post(`devices/${deviceId}/actions`, data).then(
            res =>{
                console.log(res.data);
            }
        ).catch(err => console.log(err));

    }

    const onPropValueChange = (index, newValue) => {
        const data = action;
        data.input.properties[index].currentValue = newValue
        setAction(JSON.parse(JSON.stringify(data)))
    }


    return (
        <Dialog
        fullWidth={true}
        maxWidth={'sm'}
        open={isOpen} 
        onClose={() => setIsOpen(false)}
        className="component-dialog"
    >
        <DialogTitle>{action.name}</DialogTitle>
        <DialogContent>
            <div className='component-dialog-homeName'>
                <DialogContentText>
                    {action.description}
                </DialogContentText>
                
                {
                    action.input?.properties.map((prop, index) => (
                    <div key={index}>
                        <TextField
                            autoFocus={index===0}
                            id={prop.name}
                            label={prop.name + " " + prop.unit}   
                            type={prop.type === 'integer' ? 'number' : ( prop.type === 'boolean' ? 'radio' : 'text')}
                            InputProps={{ inputProps: { min: prop.minimum, max: prop.maximum } }} 
                            variant="outlined"
                            value={prop.currentValue}
                            onChange={ event => onPropValueChange(index, event.target.value)}
                        />
                    </div>
                    ))
                }
            </div>
        </DialogContent>
        <DialogActions>
            <Button onClick={() => setIsOpen(false)}>Cancel</Button>
            <Button onClick={submitForm}>Execute</Button>
        </DialogActions>
    </Dialog>
    )
}
