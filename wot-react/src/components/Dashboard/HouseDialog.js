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
import {Delete, AddBox, RemoveCircle} from '@mui/icons-material';
import { useAuth } from "../../contexts/authcontext";

const userRoles = ['OWNER', 'MEMBER', 'CHILD', 'GUEST']

export default function HouseDialog({isDialogOpen ,setIsDialogOpen, submitForm, formData, setFormData, isEdit, deleteHome}) {

    const { currentUser } = useAuth();


    const onUserIdChange = (index, newName) => {
        const usersState = formData.users;
        usersState[index].userId = newName;
        setFormData({...formData, users:usersState})
    }

    const onUserRoleChange = (index, newRole) => {
        const usersState = formData.users;
        usersState[index].userRole = newRole;
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
                            isEdit ?
                            formData.users.map((user, index) => {
                                if( user.userId === currentUser.id)
                                    return (
                                        <div 
                                            className='component-dialog-usersName-row'
                                            key={index}
                                        >
                                            <IconButton disabled>
                                                <RemoveCircle className='component-dialog-usersName-row-removeCircle'/>
                                            </IconButton>
                                            <TextField
                                                id={`user${index}`}
                                                key={index}
                                                label="Username"    
                                                type="text"
                                                variant="standard"
                                                value={currentUser.username}
                                                disabled
                                                onChange={(event) => onUserIdChange(index, event.target.value)}
                                            />
                                            <TextField
                                                select
                                                id={`role-user${index}`}
                                                label="Role"
                                                variant="standard"
                                                value={user.userRole}
                                                onChange={(event) => onUserRoleChange(index, event.target.value)}
                                            >
                                                {userRoles.map((option) => (
                                                    <MenuItem key={option} value={option}>
                                                        {option}
                                                    </MenuItem>
                                                ))}
                                            </TextField>
                                        </div>
                                        )
                                else
                                    return null
                            })
                            :
                            <></>
                        }
                        {
                            formData.users.map((user, index) => {
                                if( user.userId !== currentUser.id)
                                    return (
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
                                            label="User Id"    
                                            type="text"
                                            variant="standard"
                                            value={user.userId}
                                            onChange={(event) => onUserIdChange(index, event.target.value)}
                                        />
                                        <TextField
                                            select
                                            id={`role-user${index}`}
                                            label="Role"
                                            variant="standard"
                                            value={user.userRole}
                                            onChange={(event) => onUserRoleChange(index, event.target.value)}
                                        >
                                            {userRoles.map((option) => (
                                                <MenuItem key={option} value={option}>
                                                    {option}
                                                </MenuItem>
                                            ))}
                                        </TextField>
                                    </div>
                                )
                                else
                                    return null
                            })
                        }
                        <div className='component-dialog-usersName-row'>
                            <IconButton 
                                onClick={()=>{
                                    setFormData({...formData,users:[...formData.users,{userId: '', userRole: 'GUEST'}]})
                                }}>
                                <AddBox color="success"/>
                            </IconButton>
                        </div>

                    </div>
                    
                </DialogContent>
                <DialogActions>
                    { isEdit && 
                    <Button 
                        id="deleteHomeBtn"
                        variant="outlined" 
                        startIcon={<Delete />}
                        color="error"
                        onClick={deleteHome}
                        sx ={{}}
                    >
                        Delete
                    </Button>
                    }
                    <Button onClick={() => setIsDialogOpen(false)}>Cancel</Button>
                    <Button onClick={submitForm}>{isEdit ? 'Edit' : 'Add'}</Button>
                </DialogActions>
            </Dialog>
    )
}
