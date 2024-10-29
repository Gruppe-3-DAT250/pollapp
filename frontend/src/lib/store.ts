import { writable } from 'svelte/store';

function createUserStore() {
    const { subscribe, set } = writable({
        username: typeof window !== 'undefined' ? localStorage.getItem('username') : null,
    });

    return {
        subscribe,
        // @ts-ignore
        setUsername: (name) => {
            set({ username: name });
            if (typeof window !== 'undefined') {
                localStorage.setItem('username', name);
            }
        }
    };
}

export const userStore = createUserStore();