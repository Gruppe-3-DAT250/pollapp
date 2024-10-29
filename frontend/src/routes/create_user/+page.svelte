<script>
    import { goto } from '$app/navigation';
    import { userStore } from '$lib/store';
    import users from '../../data/fake_users.json';


    let username = '';
    let password = '';
    let checkPassword = '';
    let email = '';
    let success = false;
    let error = '';

    const baseUrl = "http://localhost:8080";

    async function createUser() {
        // add function to check if username is taken

        if (password !== checkPassword) {
            error = "Passwords do not match.";
            return;
        }

        const newUser = {
            username: username,
            email: email,
            password: password,
            polls: [],
            votes: [],
            createdAt: new Date().toISOString(),
        };

        try {
            const response = await fetch(`${baseUrl}/v1/api/users`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newUser)
            });

            if (response.ok) {
                success = true;
                userStore.setUsername(newUser.username);
                goto('/polls');
            } else {
                const responseData = await response.json();
                error = responseData.message || "Error creating user";
            }
        } catch (err) {
            error = "Server error";
        }
    }

    function goToSignIn() {
        goto('/');
    }
</script>

<div class="container">
    <div class="user">
        <h2>Create User</h2>

        <input type="text" bind:value={username} placeholder="Username" required />
        <input type="password" bind:value={password} placeholder="Password" required />
        <input type="password" bind:value={checkPassword} placeholder="Retype password" required />
        <input type="email" bind:value={email} placeholder="Email" required />

        {#if error}
            <p style="color: red;">{error}</p>
        {/if}

        <button on:click={createUser}>Create User</button>

        <p>Already have an account?<p>

        <button on:click={goToSignIn} style="cursor: pointer; margin: -20px 0 0 -10px; color: blue; background: none; border: none; text-decoration: underline; text-align: left">
            Click here to sign in.
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