<!-- this is the page for creating a user -->

<script>
    import { goto } from "$app/navigation";
    import { authStore } from "$lib/store.ts";

    let username = "";
    let password = "";
    let checkPassword = "";
    let email = "";
    let error = "";

    const baseUrl = "http://localhost";

    async function createUser() {

        // Sanitize user inputs
        function sanitize(value) {
            return value.replace(/[&<>"'`=\/]/g, (character) => {
                return `&#${character.charCodeAt(0)};`;
            });
        }

        // Sanitize inputs before proceeding
        const sanitizedUsername = sanitize(username);
        const sanitizedEmail = sanitize(email);

        // add function to check if username is taken
        if (!/^[a-zA-Z0-9_]+$/.test(sanitizedUsername)) {
            error = "Username can only contain letters, numbers, and underscores.";
            return;
        }
        if (!/^\S+@\S+\.\S+$/.test(sanitizedEmail)) {
            error = "Please enter a valid email.";
            return;
        }
        if (password.length < 8 || !/[A-Z]/.test(password) || !/[a-z]/.test(password) || !/[0-9]/.test(password)) {
            error = "Password must be at least 8 characters long and contain upper and lower case letters and numbers.";
            return;
        }

        if (password !== checkPassword) {
            error = "Passwords do not match.";
            return;
        }



        const newUser = {
            username: sanitizedUsername,
            email: sanitizedEmail,
            password: password,
            polls: [],
            votes: [],
            createdAt: new Date().toISOString(),
        };

        try {
            const response = await fetch(`${baseUrl}/api/v1/users/register`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(newUser),
            });

            if (response.ok) {
                const data = await response.json();
                authStore.setToken(data.token);
                goto("/polls");
            } else {
                const responseData = await response.json();
                error = responseData.message || "Error creating user";
            }
        } catch (err) {
            error = "Server error";
        }
    }

    function goToSignIn() {
        goto("/");
    }
</script>

<div class="container">
    <div class="user">
        <h2>Create User</h2>

        <input
            type="text"
            bind:value={username}
            placeholder="Username"
            required
        />
        <input
            type="password"
            bind:value={password}
            placeholder="Password"
            required
        />
        <input
            type="password"
            bind:value={checkPassword}
            placeholder="Retype password"
            required
        />
        <input type="email" bind:value={email} placeholder="Email" required />

        {#if error}
            <p style="color: red;">{error}</p>
        {/if}

        <button on:click={createUser}>Create User</button>

        <p>Already have an account?</p>
        <p>
            <button
                on:click={goToSignIn}
                style="cursor: pointer; margin: -20px 0 0 -10px; color: blue; background: none; border: none; text-decoration: underline; text-align: left"
            >
                Click here to sign in.
            </button>
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
