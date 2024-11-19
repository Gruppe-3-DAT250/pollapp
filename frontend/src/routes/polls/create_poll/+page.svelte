<!-- this is the page for creating polls -->


<script>
    import { userStore } from '$lib/store.js';
    import pollsData from '../../../data/fake_polls.json';
    import { goto } from '$app/navigation';
  import { handleEvents } from '$lib/handleEvents';

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
        handleEvents('createPoll', {
            username: $userStore.username,
            pollData: pollData
        });
    }


</script>

<div class="nav-bar">
    <span on:click={() => goto('/polls')} class="nav-item">Polls</span>
    <span class="nav-item active">Create Poll</span>
</div>

<div class="container">
    <div class="poll">
        <h2>Create New Poll</h2>
        <form on:submit|preventDefault={createPoll} >
            <label for="question" class="question">Question:</label>
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

    .add-option, .submit-button {
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

    .question{
        font-size: 1.5rem;
    }

</style>
