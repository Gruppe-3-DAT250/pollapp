
export const handleEvents = async (eventType, details) => {
    const event = {
        eventType,
        details,
        time : new Date().toISOString()
    }
    const baseUrl = "http://localhost:8080"
    try{
        const response =  await fetch(`${baseUrl}/api/v1/analytics/track`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(event),

        })

        if (response.ok){
            console.log("Event tracked successfully")
        }
        else{
            console.log("Error tracking event")
        }
    } catch (error) {
        console.error("Error tracking event:", error);
    }
}