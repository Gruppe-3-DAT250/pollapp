
<!-- this is the page for showing individual polls. -->

<script lang="ts">
    import { onDestroy, onMount } from "svelte";
    import { goto } from "$app/navigation";
    import { page } from "$app/stores";
    import { authStore } from "$lib/store";

    let poll = null;
    let pollId;
    let authToken;
    let userVote = null;
    let unsubscribe;
    let isExpired;
    let userId: number;


    $: pollId = $page.params.id ? parseInt($page.params.id) : null;

    const baseUrl = "http://localhost";


    async function fetchVoteOptions() {
        try {
            const response = await fetch(
                `${baseUrl}/api/v1/polls/${pollId}/options`,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${authToken}`,
                    },
                },
            );
            if (response.ok) {
                poll.options = await response.json();
            } else {
                console.error(
                    "Failed to fetch vote options:",
                    response.statusText,
                );
            }
        } catch (error) {
            console.error("Error fetching vote options:", error);
        }
    }

    async function fetchVoteOptionCount() {
        try {
            const response = await fetch(
                `${baseUrl}/api/v1/polls/${pollId}/vote-counts`,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${authToken}`,
                    },
                },
            );
            if (response.ok) {
                const voteCounts = await response.json();
                poll.options = poll.options.map(option => {
                    const voteCount = voteCounts[option.caption] || 0;
                    return { ...option, voteCount };
                });

                poll = { ...poll };
            } else {
                console.error("Failed to fetch vote counts:", response.statusText);
            }
        } catch (error) {
            console.error("Error fetching vote counts:", error);
        }
    }

    onMount(async () => {
        unsubscribe = authStore.subscribe((value) => {
            authToken = value.authToken;
        });

        try {
            await fetchUserId();

            const response = await fetch(`${baseUrl}/api/v1/polls/${pollId}`, {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${authToken}`,
                },
            });

            if (response.ok) {
                poll = await response.json();
                isExpired = new Date(poll.validUntil) < new Date();

                if (!isExpired) {
                    const voteResponse = await fetch(
                        `${baseUrl}/api/v1/polls/${pollId}/votes/self`,
                        {
                            method: "GET",
                            headers: {
                                Authorization: `Bearer ${authToken}`,
                            },
                        },
                    );

                    userVote =
                        voteResponse.ok && voteResponse.status !== 204
                            ? await voteResponse.json()
                            : null;
                }

                await fetchVoteOptions();
                await fetchVoteOptionCount();
            } else {
                console.error("Poll not found:", response.statusText);
                poll = null;
            }
        } catch (error) {
            console.error("Failed to fetch poll:", error);
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
            const response = await fetch(
                `${baseUrl}/api/v1/polls/${pollId}/options/${optionId}`,
                {
                    method: "POST",
                    headers: {
                        Authorization: `Bearer ${authToken.trim()}`,
                        "Content-Type": "application/json",
                    },
                },
            );

            if (response.ok) {
                await fetchVoteOptions();
                await fetchVoteOptionCount();
                userVote = optionId;
                poll = { ...poll };
            } else {
                console.error("Failed to vote:", response.statusText);
            }
        } catch (error) {
            console.error("Error making vote:", error);
        }
    }

    async function removeVote(optionId) {
        try {
            const response = await fetch(`${baseUrl}/api/v1/polls/${pollId}/votes/`, {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${authToken}`,
                },
            });

            if (response.ok) {
                userVote = null;
                await fetchVoteOptions();
                await fetchVoteOptionCount();

            } else {
                console.error("Failed to remove vote:", response.statusText);
            }
        } catch (error) {
            console.error("Error removing vote:", error);
        }
    }

    async function deletePoll() {
        const confirmDelete = window.confirm(
            "Are you sure you want to delete this poll?",
        );
        if (confirmDelete) {
            try {
                const response = await fetch(
                    `${baseUrl}/api/v1/polls/${pollId}`,
                    {
                        method: "DELETE",
                        headers: {
                            Authorization: `Bearer ${authToken}`,
                        },
                    },
                );

                if (response.ok) {
                    console.log("Poll deleted successfully.");
                    goto("/polls");
                } else {
                    console.error(
                        "Failed to delete poll:",
                        response.statusText,
                    );
                }
            } catch (error) {
                console.error("Error deleting poll:", error);
            }
        }
    }

    async function fetchUserId() {
        const baseUrl = "http://localhost";
        const url = `${baseUrl}/api/v1/users/self`;
        const response = await fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${authToken}`,
            },
        });

        if (response.ok) {
            userId = await response.json();
        } else {
            const error = await response.json();
            console.error("Error fetching user ID:", error.message);
            throw new Error(error.message);
        }
    }

    function goBack() {
        goto("/polls");
    }

</script>

<div class="container">
    {#if poll}
        <div class="poll {isExpired ? 'expired' : ''}">
            {#if isExpired}
                <p class="expired-message">Expired Poll</p>
            {/if}
            <h2>{poll.question}</h2>
            <ul class="options-list">
                {#each poll.options as option (option.id)}
                    <li class="option-box">
                        <span>{option.caption}</span>
                        <div class="vote-button-container">
                            <span class="vote-count"
                                >{option.voteCount || 0}</span
                            >
                            {#if userVote === option.id}
                                <button
                                    class="remove-vote-button"
                                    on:click={() => removeVote(option.id)}
                                >
                                    Remove Vote
                                </button>
                            {:else}
                                <button
                                    class="vote-button"
                                    on:click={() => makeVote(option.id)}
                                    disabled={userVote !== null || isExpired}
                                >
                                    Vote
                                </button>
                            {/if}
                        </div>
                    </li>
                {/each}
            </ul>
            <button on:click={goBack} class="back-button">Back to Polls</button>


            {#if poll.owner && parseInt(poll.owner.id) === parseInt(userId)}
                <button on:click={deletePoll} class="delete-button" >Delete Poll</button>
            {/if}
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

    .poll.expired {
        background-color: #ffcccc;
        color: #ff0000;
    }

    .options-list {
        list-style-type: none;
        padding: 0;
    }

    .expired-message {
        font-weight: bold;
        font-size: 2rem;
        text-decoration: underline;
        color: #ff0000;
        margin-bottom: 10px;
        text-align: center;
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
        font-size: 2rem;
    }

    .vote-button-container {
        display: flex;
        align-items: center;

    }

    .vote-count {
        margin-right: 10px;
        font-size: 2rem;
        display: inline;
    }

    .delete-button,
    .back-button,
    .vote-button,
    .remove-vote-button {
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

    .delete-button {
        background-color: #ff0000;
        width: auto;
    }

    .back-button:hover, .vote-button:hover, .remove-vote-button {
        background-color: darkgray;
    }

    h2 {
        font-size: 2rem;
    }
</style>
