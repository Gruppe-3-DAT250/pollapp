export async function fetchPolls() {
    const baseUrl = "http://localhost:8080";
    const url = `${baseUrl}/v1/api/polls/get_polls`;

    const response = await fetch(url, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    });

    if (response.ok) {
        return await response.json();
    } else {
        const error = await response.json();
        console.error(error.message);
        throw new Error(error.message);
    }
}