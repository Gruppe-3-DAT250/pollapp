<!-- this is the page for creating or logging in as user -->


<script>
    import { goto } from '$app/navigation';
    import { usernameStore } from '$lib/store.js';

    let username = '';
    let success = false;
    let error = '';

    async function createUser() {
        await new Promise((resolve) => setTimeout(resolve, 1000)); // replace
        success = true;

        usernameStore.set(username);
        await goto('/polls');
    }
</script>

<div class="user">
    <h2>Create User</h2>
    <input type="text" bind:value={username} placeholder="Username" required />
    <button on:click={createUser}>Create User</button>
</div>

<style>
    .user {
        background-color: lightblue;
        padding: 30px;
        border-radius: 8px;
    }
</style>
