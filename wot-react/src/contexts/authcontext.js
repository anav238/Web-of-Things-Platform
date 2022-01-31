import axios from 'axios'
import React, { useContext, useState, useEffect } from 'react'

const AuthContext = React.createContext(undefined)

export function useAuth() {
    return useContext(AuthContext)
}

export function AuthProvider({children}) {
    const initialUser = () => JSON.parse(window.localStorage.getItem("currentUser"));
    const [currentUser, setCurrentUser] = useState(initialUser)

    function signup(username, email, password){
        const data = {
            username: username,
            password: password,
            email: email
        }
        return axios.post('users', data)
    }

    function login(username, password){
        const data = {
            username: username,
            password: password
        }

        return axios.post('users/authenticate', data).then(
            res => {
                const userId = JSON.parse(atob(res.data.split('.')[1]))?.jti;
                setCurrentUser({
                    id: userId,
                    username: username,
                    token: res.data
                })
                return res.status;
            }
        ).catch(err =>{
            return err.response.status
        })
    }

    function logout(){
        setCurrentUser(null)
    }


    useEffect(() =>{
        window.localStorage.setItem('currentUser', JSON.stringify(currentUser))
    },[currentUser])

    const value = {
        currentUser,
        setCurrentUser,
        signup,
        login,
        logout
    }
    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    )
}

export default AuthContext
