<script>
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';
    import pollsData from '../../../data/fake_polls.json';
    import { page } from '$app/stores';

    let poll = null;
    const {pollId} = $page.params;

    onMount(() => {
        if (pollId) {
            const foundPoll = pollsData.find(p => p.id === parseInt(pollId));
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

{#if poll}
    <div class="poll-details">
        <h2>{poll.question}</h2>
        <ul>
            {#each poll.options as option}
                <li>{option.caption}</li>
            {/each}
        </ul>
        <button on:click={goBack}>Back to Polls</button>
    </div>
{:else}
    <p>Poll not found.</p>
{/if}

<style>
    .poll-details {
        background-color: lightblue;
        padding: 30px;
        border-radius: 8px;
    }
</style>
