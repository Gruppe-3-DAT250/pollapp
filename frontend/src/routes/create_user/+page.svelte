<script>
    import { goto } from '$app/navigation';
    import { userStore } from '$lib/store.js';
    import users from '../../data/fake_users.json';  // Assuming the json is in $lib directory


    let username = '';
    let password = '';
    let checkPassword = '';
    let email = '';
    let success = false;
    let error = '';

    async function createUser() {
        const existingUser = users.find(user => user.username === username);
        if (existingUser){
            error = "Username taken. Log in or try again."
        }
        else{
            if (password === checkPassword) {
                const newUser = {
                    username: username,
                    email: email,
                    password: password,
                    polls: [],
                    votes: [],
                    createdAt: new Date().toISOString(),
                };

                try {
                    const response = await fetch('v1/api/user', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(newUser)
                    });

                    if (response.ok) {
                        success = true;
                        userStore.set(username);
                        goto('/polls');
                    } else {
                        const responseData = await response.json();
                        error = responseData.message || "Error creating user";
                    }
                } catch (err) {
                    error = "Server error";
                }
            } else{
                error = "Password doesnt match";
            }
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

        <p>
            <p>Already have an account?<p>
            <a on:click={goToSignIn} style="cursor: pointer; color: blue; text-decoration: underline;">
                Click here to sign in.
            </a>
        </p>
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