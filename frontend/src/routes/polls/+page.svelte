
<!-- this is the page for showing all polls -->

<script>
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';
    import pollsData from '../../data/fake_polls.json';

    let polls = [];

    onMount(() => {
        polls = pollsData; // remove fetch for API call
    });
    function selectPoll(id) {
        goto(`/polls/${id}`);
    }

    function goToCreatePoll(){
        goto('/polls/create-poll');
    }

</script>

<div class="poll">
    {#if polls.length > 0}
        <h2>Polls</h2>
        <button on:click={goToCreatePoll}>Create Poll</button>

        {#each polls as poll}
            <div class="poll-box">
                <p>{poll.question}</p>
                <button on:click={() => selectPoll(poll.id)}>Choose Poll</button>
            </div>
        {/each}
    {:else}
        <h3>No polls available</h3>
    {/if}
</div>

<style>
    .poll {
        background-color: lightblue;
        padding: 30px;
        border-radius: 8px;
    }
    .poll-box {
        background-color: white;
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 10px;
    }
</style>
