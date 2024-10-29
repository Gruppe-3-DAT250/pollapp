
<!-- this is the page for showing individual polls. -->

<script>
    import {onDestroy, onMount} from 'svelte';
    import {goto} from '$app/navigation';
    import {page} from '$app/stores';
    import {fetchPolls} from '$lib/api';
    import {userStore} from '$lib/store';


    let poll = null;
    let pollId;
    let username;
    let userVote = null;
    let unsubscribe;

    console.log(username)

    $: pollId = $page.params.id ? parseInt($page.params.id) : null;

    const baseUrl = "http://localhost:8080";


    onMount(async () => {
        unsubscribe = userStore.subscribe(value => {
            username = value.username;
        });

        try {
            const data = await fetchPolls();


            if (pollId !== undefined) {
                const foundPoll = data.find(p => p.id === pollId);
                if (foundPoll) {
                    poll = foundPoll;

                    const params = new URLSearchParams({
                        username: username,
                        pollId: pollId
                    });

                    const voteResponse = await fetch(`${baseUrl}/v1/api/vote/hasVoted?${params.toString()}`, {
                        method: 'GET',
                    });
                    if (voteResponse.ok) {
                        if (voteResponse.status === 204) {
                            userVote = null;
                        } else {
                            userVote = await voteResponse.json()
                        }
                    } else {
                        console.error("Failed to check if user has voted:", voteResponse.statusText);
                    }
                } else {
                    poll = null;
                }
            }
        } catch (error) {
            console.error("Failed to fetch polls:", error);
            poll = null;
        }

    });

    onDestroy(() => {
        if (unsubscribe) {
            unsubscribe();
        }
    });

    async function makeVote(optionId) {
        try {
            const params = new URLSearchParams({
                username: username,
                pollId: pollId
            });

            const response = await fetch(`${baseUrl}/v1/api/vote/${optionId}?${params.toString()}`, {
                method: 'POST',
            });

            if (response.ok) {
                const contentType = response.headers.get("content-type");
                if (contentType && contentType.includes("application/json")) {
                    const updatedOption = await response.json();
                    const option = poll.options[optionId];

                    if (option) {
                        option.votes.push(updatedOption);
                        userVote = option.id;
                    } else {
                        console.error("Option not found:", optionId);
                    }

                    poll = {...poll};
                }
            } else {
                console.error("Failed to vote:", response.statusText);
            }
        } catch (error) {
            console.error("Error making vote:", error);
        }
    }

    async function removeVote(optionId) {
        try {
            const params = new URLSearchParams({
                username: username,
                pollId: pollId
            });

            const response = await fetch(`${baseUrl}/v1/api/vote/${optionId}?${params.toString()}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                let option = poll.options[optionId];
                if (option) {
                    option = {
                        ...option,
                        votes: option.votes.filter(vote => vote.user.username !== username),
                    };


                    poll = {
                        ...poll,
                        options: {
                            ...poll.options,
                            [optionId]: option
                        }
                    };
                    userVote = null;
                }

            } else {
                console.error("Failed to remove vote:", response.statusText);
            }
        } catch (error) {
            console.error("Error removing vote:", error);
        }
    }


    function goBack() {
        goto('/polls');
    }
</script>

<div class="container">
    {#if poll}
        <div class="poll">
            <h2>{poll.question}</h2>
            <ul class="options-list">
                {#each Object.values(poll.options) as option (option.id)}
                    <li class="option-box">
                        <span>{option.caption}</span>
                        <div class="vote-button-container">
                            <span class="vote-count">{option.votes?.length || 0}</span>
                            {#if userVote === option.id }
                                <button class="remove-vote-button" on:click={() => removeVote(option.id)}>
                                    Remove Vote
                                </button>
                            {:else}
                                <button class="vote-button" on:click={() => makeVote(option.id)} disabled={userVote !== null}>
                                    Vote
                                </button>
                            {/if}
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

    .back-button, .vote-button, .remove-vote-button {
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

    .back-button:hover, .vote-button:hover, .remove-vote-button {
        background-color: darkgray;
    }

    h2 {
        font-size: 2rem;
    }
</style>
