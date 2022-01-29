import * as React from 'react';
import './login.scss'
import {Logo2} from './../../assets/logo'
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import {Link, useNavigate} from "react-router-dom";
import {useAuth} from "../../contexts/authcontext";
import {useEffect, useState} from "react";
import {Alert, AlertTitle} from "@mui/material";


const theme = createTheme();

export default function Login() {

    const {login} = useAuth()
    const [error, setError] = useState('')
    const [loading, setLoading] = useState(false)
    const navigate = useNavigate()

    const handleSubmit = async (event) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        try {
            setError("")
            setLoading(true)
            const response = await login(data.get('username'), data.get('password'))
            switch (response) {
                case 200:
                    navigate("/dashboard")
                    login.cancel()
                    break;
                case 401:
                    setError('Username or password incorrect')
                    break;
                default:
            }
        } catch {
            setError('Failed')
        }

        setLoading(false)
    };

    useEffect(() => {
        setTimeout(() => {
            setError(null);
        }, 5000);
    }, [error])

    return (
        <div className='login'>
            <div className='login-form'>
                <ThemeProvider theme={theme}>
                    <Container component="main" maxWidth="xs">
                        <CssBaseline />
                        <Box
                            sx={{
                                marginTop: 8,
                                display: 'flex',
                                flexDirection: 'column',
                                alignItems: 'center',
                            }}
                        >
                            <Avatar sx={{ m: 1, bgcolor: 'primary.main' }}>
                                <LockOutlinedIcon />
                            </Avatar>
                            <Typography component="h1" variant="h5">
                                Sign in
                            </Typography>
                            <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    id="username"
                                    label="Username"
                                    name="username"
                                    autoComplete="username"
                                    autoFocus
                                />
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    name="password"
                                    label="Password"
                                    type="password"
                                    id="password"
                                    autoComplete="current-password"
                                />
                                {error ?
                                    <Alert severity="error" onClose={() => {setError(null)}}>
                                        <AlertTitle>An authentication exception occurred</AlertTitle>
                                        {error}
                                    </Alert>
                                    :
                                    <FormControlLabel
                                        control={<Checkbox value="remember" color="primary"/>}
                                        label="Remember me"
                                    />
                                }
                                <Button
                                    disabled={loading}
                                    type="submit"
                                    fullWidth
                                    variant="contained"
                                    sx={{ mt: 3, mb: 2 }}
                                >
                                    Sign In
                                </Button>
                                <Grid container>
                                    <Grid item xs>
                                        <Link to="#" variant="body2">
                                            Forgot password?
                                        </Link>
                                    </Grid>
                                    <Grid item>
                                        <Link to="/register">
                                            {"Don't have an account? Sign Up"}
                                        </Link>
                                    </Grid>
                                </Grid>
                            </Box>
                        </Box>
                    </Container>
                </ThemeProvider>
            </div>
            <div className='login-img'>
                <Logo2/>
            </div>
        </div>
    );
}