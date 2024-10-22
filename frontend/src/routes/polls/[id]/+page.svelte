
<!-- this is the page for showing individual polls -->

<script>
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';
    import pollsData from '../../../data/fake_polls.json';
    import { page } from '$app/stores';

    let poll = null;
    let pollId;

    $: pollId = $page.params.id ? parseInt($page.params.id) : null; // Ensure it's parsed as a number

    onMount(() => {
        const flattenedData = pollsData.flat();

        if (pollId) {
            const foundPoll = flattenedData.find(p => p.id === pollId);
            if (foundPoll) {
                poll = foundPoll;
            } else {
                poll = null;
            }
        }
    });

    function goBack() {
        goto('/polls');
    }
</script>

<div class="container">
    {#if poll}
        <div class="poll-details">
            <h2>{poll.question}</h2>
            <ul class="options-list">
                {#each poll.options as option}
                    <li class="option-box">
                        <span>{option.caption}</span>
                        <div class="vote-button-container">
                            <span class="vote-count">{option.votes || 0} votes</span>
                            <button>Vote</button>
                        </div>
                    </li>
                {/each}
            </ul>
            <button on:click={goBack}>Back to Polls</button>
        </div>
    {:else}
        <p>Poll not found.</p>
    {/if}
</div>

<style>
    .container {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
    }
    .poll-details {
        background-color: lightblue;
        padding: 30px;
        border-radius: 8px;
        width: 500px;
    }
    .options-list {
        list-style-type: none;
        padding: 0;
    }
    .option-box {

        background-color: white;
        padding: 15px;
        border: 1px solid;
        border-radius: 5px;
        margin-bottom: 10px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .vote-button-container {
        display: flex;
        align-items: center;
    }
    .vote-count {
        margin-right: 10px;
    }

</style>
