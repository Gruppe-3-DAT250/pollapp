// @ts-ignore
export async function fetchPolls(token) {
    const baseUrl = "http://localhost:8080";
    const url = `${baseUrl}/v1/api/polls/get_polls`;

    const response = await fetch(url, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
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