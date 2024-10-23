
<!-- this is the page for showing all polls -->

<script>
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';
    import pollsData from '../../data/fake_polls.json';

    let polls = [];

    onMount(() => {
        polls = pollsData.flat(); // remove
    });
    function selectPoll(id) {
        goto(`/polls/${id}`);
    }

    function goToCreatePoll(){
        goto('/polls/create_poll');
    }

</script>

<div class="nav-bar">
    <span on:click={() => goto('/polls')} class="nav-item active">Polls</span>
    <span on:click={goToCreatePoll} class="nav-item">Create Poll</span>
</div>

<div class="container">
    <div class="poll">
        {#if polls.length > 0}
            <h2>Polls</h2>
            {#each polls as poll}
                <div class="poll-box">
                    <h1>{poll.question}</h1>
                    <button on:click={() => selectPoll(poll.id)} class="choose-button">Choose Poll</button>
                </div>
            {/each}
        {:else}
            <h3>No polls available</h3>
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

    h3 {
        font-size: 1.75rem;
    }
</style>
