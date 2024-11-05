<!-- this is the page for logging in as user -->

<script>
    import { goto } from "$app/navigation";
    import { authStore } from "$lib/store.ts";

    let username = "";
    let password = "";
    let error = "";
    const baseUrl = "http://localhost:8080";

    async function signIn() {
        try {
            const response = await fetch(`${baseUrl}/v1/api/users`);
            if (response.ok) {
                const { token } = await response.json();
                authStore.setToken(token);
                await goto("/polls");
            } else {
                error = "Invalid credentials. Please try again.";
            }
        } catch (err) {
            console.error("Login error:", err);
            error = "An error occurred. Please try again later.";
        }
    }

    function goToSignUp() {
        goto("/create_user");
    }
</script>

<div class="container">
    <div class="user">
        <h2>Sign in or create user</h2>

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

        {#if error}
            <p style="color: red;">{error}</p>
        {/if}

        <button on:click={signIn}>Sign in</button>
        <p>Are you new?</p>
        <p>
            <button
                on:click={goToSignUp}
                style="cursor: pointer; margin: -20px 0 0 -10px; color: blue; background: none; border: none; text-decoration: underline; text-align: left"
            >
                Click here to create a user.
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
