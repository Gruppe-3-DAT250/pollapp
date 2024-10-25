
<!-- this is the page for showing individual polls -->

<script>
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';
    import { page } from '$app/stores';
    import { fetchPolls } from '$lib/api';


    let poll = null;
    let pollId;

    $: pollId = $page.params.id ? parseInt($page.params.id) : null;

    onMount(async () => {
        try {
            const data = await fetchPolls();

            if (pollId !== undefined) {
                const foundPoll = data.find(p => p.id === pollId);
                if (foundPoll) {
                    poll = foundPoll;
                } else {
                    poll = null;
                }
            }
        } catch (error) {
            console.error("Failed to fetch polls:", error);
            poll = null;
        }
    });

    function goBack() {
        goto('/polls');
    }
</script>

<div class="container">
    {#if poll}
        <div class="poll">
            <h2>{poll.question}</h2>
            <ul class="options-list">
                {#each Object.values(poll.options) as option}
                    <li class="option-box">
                        <span>{option.caption}</span>
                        <div class="vote-button-container">
                            <span class="vote-count">{option.votes || 0}</span>
                            <button class="vote-button">Vote</button>
                        </div>
                    </li>
                {/each}
            </ul>
            <button on:click={goBack} class="back-button">Back to Polls</button>
        </div>
    {:else}
        <p>Poll not found.</p>
    {/if}
</div>

<style>
    body {
        background-color: #f6f8fa;
        margin: 0;
        padding: 0;
        height: 100vh;
        display: flex;
        justify-content: center;
        align-items: flex-start;
    }

    .container {
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: flex-start;
    }


    .poll {
        background-color: #e0e0e0;
        padding: 20px;
        border-radius: 5px;
        width: 50%;
        max-height: 100vh;
        overflow-y: auto;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        margin-top: 40px;
    }

    .options-list {
        list-style-type: none;
        padding: 0;
    }

    .option-box {
        background-color: white;
        padding: 15px;
        border: 1px solid #ccc;
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
        font-size: 1.5rem;
        display: inline;
    }

    .back-button, .vote-button {
        margin-top: 15px;
        padding: 10px;
        background-color: grey;
        color: white;
        border-style: solid;
        border-radius: 4px;
        width: 100%;
        cursor: pointer;
        font-size: 1.2rem;
    }

    .back-button:hover, .vote-button:hover {
        background-color: darkgray;
    }

    h2 {
        font-size: 2rem;
    }
</style>
