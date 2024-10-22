<!-- this is the page for creating polls -->


<script>
    import { userStore } from '$lib/store.js';
    import pollsData from '../../../data/fake_polls.json';
    import { goto } from '$app/navigation';

    let question = '';
    let options = [''];
    let responseMessage = '';

    function addOption() {
        options = [...options, ''];
    }

    function updateOption(index, value) {
        options[index] = value;
    }

    function createPoll() {
        const pollData = { // variables need to be changed
            question,
            publishedAt: new Date().toISOString(),
            validUntil: new Date().toISOString(), // change to userinput?
            creator: { username: $userStore.username },
            options: options.map((option, index) => ({
                caption: option,
                presentationOrder: (index + 1).toString(),
            })),
        };

        console.log("Poll created:", pollData);
        responseMessage = "Poll created!";
        // must be connected to api
    }

    function goBack(){
        goto("/polls")
    }
</script>

<div class="container">
    <div class="poll">
        <h2>Create New Poll</h2>
        <form on:submit|preventDefault={createPoll}>
            <label for="question">Question:</label>
            <input type="text" id="question" bind:value={question} required />

            <div class="options-container">
                {#each options as option, index}
                    <label for={"option" + index}>Option {index + 1}:</label>
                    <input type="text" id={"option" + index} bind:value={options[index]} on:input={e => updateOption(index, e.target.value)} required />
                {/each}
            </div>

            <button type="button" class="add-option" on:click={addOption}>Add Another Option</button>
            <button type="submit" class="submit-button">Submit</button>
            <p>{responseMessage}</p>
            <button type="button" class="submit-button" on:click={goBack}>Go back</button>
        </form>
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
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        width: 400px;
    }

    input[type="text"] {
        width: 100%;
        padding: 8px;
        margin: 5px 0;
        border: 1px solid;
        border-radius: 4px;
        box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1);
    }
    .options-container {
        display: flex;
        flex-direction: column;
        margin-bottom: 15px;
    }
    .add-option, .submit-button {
        margin-top: 15px;
        padding: 10px;
        background-color: grey;
        color: white;
        border-style: solid;
        border-radius: 4px;
        width: 100%;
    }
    button {
        cursor: pointer;
    }

</style>
