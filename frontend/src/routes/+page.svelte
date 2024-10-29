<!-- this is the page for creating or logging in as user -->


<script>
    import { goto } from '$app/navigation';
    import { userStore } from '$lib/store.ts';
    import {onMount} from "svelte";


    let username = '';
    let password = '';
    let success = false;
    let error = '';
    let users = [];

    const baseUrl = "http://localhost:8080";

    onMount(async () => {
        try {
            const response = await fetch(`${baseUrl}/v1/api/user`);
            if (response.ok) {
                users = await response.json();
            } else {
                console.error("Failed to fetch users:", response.statusText);
            }
        } catch (error) {
            console.error("Error fetching users:", error);
        }
    });

    async function signIn(){
        const existingUser = users.find(user => user.username === username);
        if (existingUser){
            if (existingUser.password === password){
                success = true;
                userStore.setUsername(existingUser.username);
                await goto('/polls');
            }
            else {
                error = "Incorrect password. Please try again.";
            }
        }
        else {
            error = "User does not exist. Create new user!"
        }
    }

    function goToSignUp(){
        goto("/create_user");
    }


</script>

<div class="container">
    <div class="user">
        <h2>Sign in or create user</h2>

        <input type="text" bind:value={username} placeholder="Username" required />
        <input type="password" bind:value={password} placeholder="Password" required />

        {#if error}
            <p style="color: red;">{error}</p>
        {/if}

        <button on:click={signIn}>Sign in</button>
        <p>Are you new?<p>
        <button on:click={goToSignUp} style="cursor: pointer; margin: -20px 0 0 -10px; color: blue; background: none; border: none; text-decoration: underline; text-align: left">
            Click here to create a user.
        </button>

    </div>
</div>

<style>
    .container {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
    }

    .user {
        background-color: lightblue;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        width: 500px;
    }

    input {
        width: 96%;
        padding: 10px;
        margin: 10px 0;
        border: 1px solid;
        border-radius: 4px;
    }

    button {
        width: 100%;
        padding: 10px;
        color: black;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        margin: 10px 0;
    }

</style>
