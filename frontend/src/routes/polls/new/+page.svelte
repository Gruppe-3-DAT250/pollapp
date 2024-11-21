<!-- this is the page for creating polls -->

<script>
    import { authStore } from "$lib/store.ts";
    import { goto } from "$app/navigation";
    import { onDestroy, onMount } from "svelte";

    let question = "";
    let validUntil = "";
    let options = [""];
    let responseMessage = "";
    let authToken;
    let unsubscribe;

    const baseUrl = "http://localhost:8080";

    onMount(async () => {
        unsubscribe = authStore.subscribe((value) => {
            authToken = value.authToken;
        });
    });

    onDestroy(() => {
        if (unsubscribe) {
            unsubscribe();
        }
    });

    function addOption() {
        options = [...options, ""];
    }

    function updateOption(index, value) {
        options[index] = value;
    }

    async function createPoll() {
        const pollData = {
            question: question,
            publishedAt: new Date().toISOString(),
            validUntil: new Date(validUntil).toISOString(),
            options: options.reduce((acc, option, index) => {
                acc[index.toString()] = {
                    caption: option,
                    presentationOrder: (index + 1).toString(),
                    id: index.toString(),
                };
                return acc;
            }, {}),
        };

        const response = await fetch(`${baseUrl}/api/v1/polls`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${authToken}`,
            },
            body: JSON.stringify(pollData),
        });

        if (response.ok) {
            responseMessage = "Poll created!";
            await goto("/polls");
        } else {
            const data = await response.json();
            console.log(data.message);
        }
    }
</script>

<div class="nav-bar">
    <a href="/polls" class="nav-item">Polls</a>
    <a href="/polls/new" class="nav-item active">Create Poll</a>
</div>

<div class="container">
    <div class="poll">
        <h2>Create New Poll</h2>
        <form on:submit|preventDefault={createPoll}>
            <label for="question" class="question">Question:</label>
            <input type="text" id="question" bind:value={question} required />

            <div class="options-container">
                {#each options as option, index}
                    <label for={"option" + index}>Option {index + 1}:</label>
                    <input
                        type="text"
                        id={"option" + index}
                        bind:value={options[index]}
                        on:input={(e) => updateOption(index, e.target.value)}
                        required
                    />
                {/each}
            </div>

            <input
                type="date"
                bind:value={validUntil}
                placeholder="Valid Until"
            />

            <button type="button" class="add-option" on:click={addOption}
                >Add Another Option</button
            >
            <button type="submit" class="submit-button">Submit</button>

            <p>{responseMessage}</p>
        </form>
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
        margin-top: 10%;
        max-height: 85vh;
        overflow-y: auto;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    }

    input[type="text"] {
        width: 100%;
        padding: 8px;
        margin: 5px 0;
        border: 1px solid;
        border-radius: 4px;
        box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1);
        font-size: 1.25rem;
    }

    .options-container {
        display: flex;
        flex-direction: column;
        margin-bottom: 15px;
    }

    .add-option,
    .submit-button {
        margin-top: 15px;
        padding: 10px;
        background-color: grey;
        color: white;
        border-style: solid;
        border-radius: 4px;
        width: 100%;
        font-size: 1.25rem;
    }

    button {
        cursor: pointer;
    }

    .question {
        font-size: 1.5rem;
    }
</style>
