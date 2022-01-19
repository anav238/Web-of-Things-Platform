import React, {useState} from 'react'
import { DataGrid } from '@mui/x-data-grid';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import {Add, AddBox, RemoveCircle} from '@mui/icons-material';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';


const pageSize = 5;
const columns = [
    { field: 'id', headerName: 'ID', width: 300 },
    { field: 'name', headerName: 'House name', width: 250 },
    { field: 'role', headerName: 'Role', width: 160,
        valueGetter: (params) => params.row.users.find( user => user.name==='Vlad Afrasinei').role
    },
    { field: 'usersNumber', headerName: 'Users', width: 140,
        valueGetter: (params) => params.row.users.length
    }, 
    { field: 'devicesNumber', headerName: 'Devices', width: 140,
        valueGetter: (params) => params.row.deviceIds.length 
    },
  ];

export default function Houses({houses, setHouses, setHouseSelected}) {
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const [formData, setFormData] = useState({name: '', users: ['']})

    const submitForm = () => {
        console.log(formData);
        
        //setIsDialogOpen(false);
        //setHouses([...houses,  apiResponse(formData)]W);
    }

    const onAddUserNameChange = (index, newName) => {
        const usersState = formData.users;
        usersState[index] = newName;
        setFormData({...formData, users:usersState})
    }

    const onAddUserRemove = (index) => {
        const usersState = formData.users;
        usersState.splice(index,1);
        setFormData({...formData, users:usersState})
    }

    return (
        <div className='component'>
            <div className='component-header'>
                <div className='component-header-addBtn'>
                    <Button 
                        variant="contained" 
                        color="success"
                        size="large"
                        onClick={() => setIsDialogOpen(true)}
                        startIcon={<Add />}>
                        New Home
                    </Button>
                </div>
            </div>

            <div className='component-dataGrid'>
                <DataGrid
                    rows={houses}
                    columns={columns}
                    autoHeight
                    pageSize={pageSize}
                    rowsPerPageOptions={[pageSize]}
                    onRowClick={(params)=> setHouseSelected(params.row)}
                    />
            </div>
            <Dialog
                fullWidth={true}
                maxWidth={'sm'}
                open={isDialogOpen} 
                onClose={() => setIsDialogOpen(false)}
                className="component-dialog"
            >
                <DialogTitle>Add new home</DialogTitle>
                <DialogContent>
                    <div className='component-dialog-homeName'>
                        <DialogContentText>
                            Please fill the form below:
                        </DialogContentText>
                        <TextField
                            autoFocus
                            id="name"
                            label="Name"    
                            type="text"
                            variant="outlined"
                            value={formData.name}
                            onChange={(event) => { setFormData({...formData,name:event.target.value})}}
                        />
                    </div>
                    <div className='component-dialog-usersName'>
                        <DialogContentText>
                            Add users:
                        </DialogContentText>
                        {
                            formData.users.map((user, index) => (
                            <div 
                                className='component-dialog-usersName-row'
                                key={index}
                            >
                                <IconButton onClick={()=> onAddUserRemove(index)}>
                                    <RemoveCircle className='component-dialog-usersName-row-removeCircle'/>
                                </IconButton>
                                <TextField
                                    id={`user${index}`}
                                    key={index}
                                    label="User name"    
                                    type="text"
                                    variant="standard"
                                    value={user}
                                    onChange={(event) => onAddUserNameChange(index, event.target.value)}
                                    />
                                
                            </div>
                            ))
                        }
                        <div className='component-dialog-usersName-row'>
                            <IconButton 
                                onClick={()=>{
                                    setFormData({...formData,users:[...formData.users,'']})
                                }}>
                                <AddBox color="success"/>
                            </IconButton>
                        </div>
                    </div>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setIsDialogOpen(false)}>Cancel</Button>
                    <Button onClick={submitForm}>Add</Button>
                </DialogActions>
            </Dialog>
        </div>
    )
}
