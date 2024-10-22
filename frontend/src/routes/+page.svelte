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

    async function createUser() {
        const existingUser = users.find(user => user.username === username);
        if (existingUser){
            error = "Username taken. Log in or try again."
        }
        else{
            const newUser = {
                username : username,
                email: "${username}@example.com",
                password: password,
                polls: [],
                votes: [],
                createdAt: new Date().toISOString(),
            };

            users.push(newUser);
            success = true;
            userStore.set(username);
            await goto('/polls')
        }
    }
</script>

<div class="user">
    <h2>Sign in or create user</h2>

    <input type="text" bind:value={username} placeholder="Username" required />
    <input type="password" bind:value={password} placeholder="Password" required />

    {#if error}
        <p style="color: red;">{error}</p>
    {/if}

    <button on:click={signIn}>Sign in</button>
    <button on:click={createUser}>Create User</button>

</div>

<style>
    .user {
        background-color: lightblue;
        padding: 30px;
        border-radius: 8px;
    }
</style>
