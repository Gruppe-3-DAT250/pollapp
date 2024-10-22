
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

<div class="container">
    <div class="poll">
        {#if polls.length > 0}
            <h2>Polls</h2>

            {#each polls as poll}
                <div class="poll-box">
                    <p>{poll.question}</p>
                    <button on:click={() => selectPoll(poll.id)} class="choose-button">Choose Poll</button>
                </div>
            {/each}
            <button on:click={goToCreatePoll} class="create-button">Create Poll</button>

        {:else}
            <h3>No polls available</h3>
        {/if}
    </div>
</div>

<style>
    .container {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;

    }
    .poll {
        background-color: lightblue;
        padding: 20px;
        border-radius: 5px;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        width: 400px;
    }
    .poll-box {
        margin-bottom: 10px;
        padding: 10px;
        border: 1px solid;
        border-radius: 4px;
        background-color: white;
    }
    button {
        cursor: pointer;
    }
    .choose-button {
        margin-top: 15px;
        padding: 10px;
        background-color: white;
        color: black;
        border-style: solid;
        border-radius: 4px;
        width: 100%;
    }
    .create-button:hover{
        background-color: darkgray;
    }
    .create-button {
        margin-top: 15px;
        padding: 10px;
        background-color: grey;
        color: white;
        border-style: solid;
        border-radius: 4px;
        width: 100%;
    }
    .create-button:hover{
        background-color: darkgray;
    }

</style>
