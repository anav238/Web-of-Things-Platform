import React, {useState} from 'react'
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import Button from '@mui/material/Button';
import {Delete, SaveAlt} from '@mui/icons-material';
import Snackbar from '@mui/material/Snackbar';
import MuiAlert from '@mui/material/Alert';
import './Profile.scss'
import { useAuth } from "../../contexts/authcontext";
import axios from 'axios'

const Alert = React.forwardRef(function Alert(props, ref) {
    return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
  });
  

export default function Profile({isDialogOpen ,closeDialog}) {

    const { currentUser, setCurrentUser, logout, login } = useAuth();
    const [userName, setUserName] = useState(currentUser.username)
    const [password, setPassword] = useState("")
    const [password2, setPassword2] = useState("")
    const [confirmPassword, setConfirmPassword] = useState("")

    const [snackbarOpen, setSnackbarOpen] = useState(false)
    const [error, setError] = useState(null)

    const payload =[{
        op: "replace",
        path: "", 
        value: ""
    }]

    const updateUsername = () => {
        payload[0].path = '/username';
        payload[0].value = userName;
        axios.patch(`users/${currentUser.id}`, payload,{
            headers: {
                'Content-Type': 'application/json-patch+json',
            }
        })
            .then( () => {
                setCurrentUser({...currentUser, username:userName});
                setError(null)
                setSnackbarOpen(true);
            })
            .catch(err => {
                setError(err.response.data)
                setSnackbarOpen(true)
            })
    }

    const updatePassword = () => {
        payload[0].path = '/password';
        payload[0].value = password;
        axios.patch(`users/${currentUser.id}`, payload,{
            headers: {
                'Content-Type': 'application/json-patch+json',
            }
        })
            .then( () => {
                setError(null)
                setSnackbarOpen(true);
                setPassword('');
                setPassword2('');
            })
            .catch(err => {
                setError(err.response.data)
                setSnackbarOpen(true)
                setPassword('');
                setPassword2('');
            })
        
    }

    const deleteAccount = () =>{

        login(currentUser.username, confirmPassword).then( res =>
            {
                if(res === 200){
                    axios.delete(`users/${currentUser.id}`)
                    .then(() => logout())
                    .catch(err => {
                        setError(err.response.data.error)
                        setSnackbarOpen(true)
                    })
                }
                else{
                    setError({status: res, error: "Invalid password"})
                    setSnackbarOpen(true)
                }
            })
        }


    const closeSnackbar = (event, reason) => {
        if (reason === 'clickaway') {
          return;
        }

        setSnackbarOpen(false);
      };

    const onCloseDialog =() => {
        setUserName(currentUser.username)
        setPassword('')
        setPassword2('')
        setConfirmPassword('')

        closeDialog();

    }
    
    return (
            <>
            <Dialog
            fullWidth={true}
            maxWidth={'sm'}
            open={isDialogOpen} 
            onClose={onCloseDialog}
            className="component-dialog"
        >
            <DialogTitle>Profile information</DialogTitle>
            <DialogContent>
                <div className='component-dialog-profile'>
                    <div className='component-dialog-profile-row'>
                        <div className='component-dialog-profile-row-textFields'>
                            <TextField
                                autoComplete="new-password"
                                id="username"
                                label="Username"    
                                type="text"
                                variant="outlined"
                                value={userName}
                                onChange={(event) => { setUserName(event.target.value)}}
                            />
                        </div>
                        <div className='component-dialog-profile-row-btn'>
                            <Button 
                                variant="contained" 
                                color="success"
                                disabled={userName===currentUser.username}
                                startIcon={<SaveAlt />}
                                onClick={updateUsername}>
                                Save
                            </Button>
                        </div>
                    </div>
                    
                    <div className='component-dialog-profile-row'>
                        <div className='component-dialog-profile-row-textFields'>
                            <TextField
                                autoComplete="new-password"
                                id="new-password"
                                label="Change Password"    
                                type="password"
                                variant="outlined"
                                value={password}
                                onChange={(event) => { setPassword(event.target.value)}}
                            />
                            <TextField
                                autoComplete="new-password"
                                id="new-password2"
                                label="Repeat Password"    
                                type="password"
                                variant="outlined"
                                value={password2}
                                onChange={(event) => { setPassword2(event.target.value)}}
                                error={password!==password2}
                            />
                        </div>
                        <div className='component-dialog-profile-row-btn'>
                            <Button 
                                variant="contained" 
                                color="success"
                                disabled={ password==='' || password!==password2}
                                startIcon={<SaveAlt />}
                                onClick={updatePassword}>
                                Save
                            </Button>
                        </div>
                    </div>

                    <div className='component-dialog-profile-row'>
                        <div className='component-dialog-profile-row-textFields'>
                            <TextField
                                autoComplete="new-password"
                                id="confirmPassword"
                                label="Confirm Password"    
                                type="password"
                                variant="outlined"
                                value={confirmPassword}
                                onChange={(event) => { setConfirmPassword(event.target.value)}}
                            />
                        </div>
                        <div className='component-dialog-profile-row-btn'>
                        <Button 
                            variant="outlined" 
                            startIcon={<Delete />}
                            color="error"
                            onClick={deleteAccount}
                            disabled={confirmPassword===""}
                        >
                            Delete
                        </Button>
                        </div>
                    </div>
                </div>
            </DialogContent>
            <DialogActions>
                <Button onClick={onCloseDialog}>Close</Button>
            </DialogActions>
        </Dialog>
        <Snackbar
            open={snackbarOpen}
            autoHideDuration={5000}
            onClose={closeSnackbar}
            message="Note archived"
        >
            {
            !error ?
                <Alert onClose={closeSnackbar} severity="success" sx={{ width: '100%' }}>
                    Updated successfully
                </Alert>
            :
                <Alert onClose={closeSnackbar} severity="error" sx={{ width: '100%' }}>
                    {error.status ?? 'Error'} : {error.error ?? "unknown"}
                </Alert>
            }
        </Snackbar>
    </>
    )
}
