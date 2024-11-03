
<!-- this is the page for showing all polls -->

<script>
    import {onMount} from 'svelte';
    import {goto} from '$app/navigation';
    import {authStore} from "$lib/store.ts";


    let activePolls = [];
    let expiredPolls = [];
    let unsubscribe;
    let authToken;

    async function fetchPolls() {
        const baseUrl = "http://localhost:8080";
        const url = `${baseUrl}/v1/api/polls/get_polls`;

        const response = await fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${authToken}`,
            },
        });

        if (response.ok) {
            return await response.json();
        } else {
            const error = await response.json();
            console.error(error.message);
            throw new Error(error.message);
        }
    }

    onMount(async () => {
        try {
            unsubscribe = authStore.subscribe(value => {
                authToken = value.authToken;
            });

            const allPolls = await fetchPolls();

            const now = new Date();
            activePolls = allPolls
                .filter(poll => new Date(poll.validUntil) >= now)
                .sort((a, b) => new Date(b.publishedAt) - new Date(a.publishedAt));
            expiredPolls = allPolls
                .filter(poll => new Date(poll.validUntil) < now)
                .sort((a, b) => new Date(b.publishedAt) - new Date(a.publishedAt));


        } catch (error) {
            console.error("Failed to fetch polls:", error);
        }
    });

    function selectPoll(id) {
        goto(`/polls/${id}`);
    }


</script>


<div class="nav-bar">
    <a href="/polls" class="nav-item active">Polls</a>
    <a href="/polls/create_poll" style="cursor: pointer;" class="nav-item">Create Poll</a>
</div>

<div class="container">
    <div class="poll">
        {#if activePolls.length > 0}
            <h2>Polls</h2>
            {#each activePolls as poll}
                <div class="poll-box">
                    <h1>{poll.question}</h1>
                    <button on:click={() => selectPoll(poll.id)} class="choose-button">Choose Poll</button>
                </div>
            {/each}
        {:else}
            <h3>No active polls available</h3>
        {/if}

        {#if expiredPolls.length > 0}
            <h2 class="h2-expired">Expired Polls</h2>
            {#each expiredPolls as poll}
                <div class="poll-box expired">
                    <h1>{poll.question}</h1>
                    <button on:click={() => selectPoll(poll.id)} class="choose-button">View Poll</button>
                </div>
            {/each}
        {:else}
            <h3>No expired polls available</h3>
        {/if}
    </div>
</div>

<style>
    .nav-bar {
        position: fixed;
        top: 0;
        width: 100%;
        display: flex;
        justify-content: center;
        background-color: #222;
        padding: 20px;
        font-size: 1.5rem;
        color: white;
        z-index: 1000;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
    }

    .nav-item {
        color: white;
        margin: 0 25px;
        padding: 15px;
        cursor: pointer;
        text-decoration: none;
    }

    .nav-item.active {
        border-bottom: 3px solid white;
    }

    .nav-item:hover {
        background-color: #555;
    }

    .container {
        display: flex;
        justify-content: center;
        align-items: flex-start;
        margin-top: 80px;
    }

    .poll {
        background-color: #e0e0e0;
        padding: 20px;
        border-radius: 5px;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        width: 100%;
        height: 85vh;
        overflow-y: auto;
    }

    .poll-box {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;
        padding: 20px;
        border: 1px solid;
        border-radius: 8px;
        background-color: white;
        font-size: 1.25rem;
    }

    button {
        cursor: pointer;
        font-size: 1.25rem;
        padding: 15px;
    }

    .choose-button {
        margin-top: 20px;
        padding: 15px;
        background-color: white;
        color: black;
        border-style: solid;
        border-radius: 8px;
        width: 20%;
        font-size: 1.25rem;
        margin-left: 20px;
    }

    .choose-button:hover {
        background-color: darkgray;
    }

    h1 {
        font-size: 1.5rem;
    }

    h2 {
        font-size: 2rem;
    }

    .h2-expired {
        font-size: 2rem;
        color: #ff0000;
    }

    h3 {
        font-size: 1.75rem;
    }

    .expired {
        background-color: #ffcccc;
        color: #ff0000;
    }

</style>
