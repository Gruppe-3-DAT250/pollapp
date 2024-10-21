<script>
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';
    import { writable } from 'svelte/store';
    import pollsData from '../../data/fake_polls.json';

    let polls = [];
    let selectedPoll = null;

    onMount(() => {
        polls = pollsData; // remove
    });

    function selectPoll(id) {
        selectedPoll = polls.find(poll => poll.id === id);
    }

</script>

<div class="poll">
    {#if polls.length > 0}
        <h2>Polls</h2>
        {#each polls as poll}
            <div class="poll-box">
                {poll.question}
                <button on:click={() => selectPoll(poll.id)}>Choose Poll</button>
            </div>
        {/each}
        {#if selectedPoll}
            <h3>{selectedPoll.question}</h3>
            <ul>
                {#each selectedPoll.options as option}
                    <li>{option.caption}</li>
                {/each}
            </ul>
            <button on:click={() => (selectedPoll = null)}>Back</button>
        {/if}
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
