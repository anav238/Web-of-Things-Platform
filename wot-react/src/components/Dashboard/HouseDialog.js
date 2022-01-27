import React from 'react'
import TextField from '@mui/material/TextField';
import MenuItem from '@mui/material/MenuItem';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import IconButton from '@mui/material/IconButton';
import Button from '@mui/material/Button';
import {AddBox, RemoveCircle} from '@mui/icons-material';

const userRoles = ['Owner', 'Member', 'Child']

export default function HouseDialog({isDialogOpen ,setIsDialogOpen, submitForm, formData, setFormData, isEdit}) {

    const onUserNameChange = (index, newName) => {
        const usersState = formData.users;
        usersState[index].name = newName;
        setFormData({...formData, users:usersState})
    }

    const onUserRoleChange = (index, newRole) => {
        const usersState = formData.users;
        usersState[index].role = newRole;
        setFormData({...formData, users:usersState})
    }

    const onUserRemove = (index) => {
        const usersState = formData.users;
        usersState.splice(index,1);
        setFormData({...formData, users:usersState})
    }

    return (
        <Dialog
                fullWidth={true}
                maxWidth={'sm'}
                open={isDialogOpen} 
                onClose={() => setIsDialogOpen(false)}
                className="component-dialog"
            >
                <DialogTitle>{isEdit ? 'Edit your home' : 'Add new home'}</DialogTitle>
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
                                <IconButton onClick={()=> onUserRemove(index)}>
                                    <RemoveCircle className='component-dialog-usersName-row-removeCircle'/>
                                </IconButton>
                                <TextField
                                    id={`user${index}`}
                                    key={index}
                                    label="User name"    
                                    type="text"
                                    variant="standard"
                                    value={user.name}
                                    onChange={(event) => onUserNameChange(index, event.target.value)}
                                />
                                 <TextField
                                    select
                                    id={`role-user${index}`}
                                    label="Role"
                                    variant="standard"
                                    value={user.role}
                                    onChange={(event) => onUserRoleChange(index, event.target.value)}
                                >
                                    {userRoles.map((option) => (
                                        <MenuItem key={option} value={option}>
                                            {option}
                                        </MenuItem>
                                    ))}
                                </TextField>
                            </div>
                            ))
                        }
                        <div className='component-dialog-usersName-row'>
                            <IconButton 
                                onClick={()=>{
                                    setFormData({...formData,users:[...formData.users,{name: '', role: ''}]})
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
    )
}
