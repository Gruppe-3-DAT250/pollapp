<!-- this is the page for creating polls -->


<script>
    import { userStore } from '$lib/store.js';
    import pollsData from '../../data/fake_polls.json';

    let question = '';
    let options = [''];
    let responseMessage = '';

    function addOption() {
        options.push('');
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
</script>

<div class="poll">
    <h2>Create New Poll</h2>
    <form on:submit|preventDefault={createPoll}>
        <label for="question">Question:</label>
        <input type="text" id="question" bind:value={question} required />

        {#each options as option, index}
            <label for={"option" + index}>Option {index + 1}:</label>
            <input type="text" id={"option" + index} bind:value={options[index]} on:input={e => updateOption(index, e.target.value)} required />
        {/each}

        <button type="button" on:click={addOption}>Add Another Option</button>
        <button type="submit">Submit</button>
        <p>{responseMessage}</p>
    </form>
</div>

<style>
    .poll {
        background-color: lightblue;
        padding: 30px;
        border-radius: 8px;
    }
</style>
