<!-- this is the page for creating or logging in as user -->


<script>
    import { goto } from '$app/navigation';
    import { userStore } from '$lib/store.js';
    import users from '../data/fake_users.json';  // Assuming the json is in $lib directory


    let username = '';
    let password = '';
    let success = false;
    let error = '';

    async function signIn(){
        const existingUser = users.find(user => user.username === username);
        if (existingUser){
            if (existingUser.password === password){
                success = true;
                userStore.set(username);
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
        <a on:click={goToSignUp} style="cursor: pointer; color: blue; text-decoration: underline;">
            Click here to create a user.
        </a>

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
