import { writable } from 'svelte/store';

function createAuthStore() {
    const { subscribe, set } = writable({
        authToken: typeof window !== 'undefined' ? localStorage.getItem('authToken') : null,
    });

    return {
        subscribe,
        // @ts-ignore
        setToken: (token) => {
            set({ authToken: token });
            if (typeof window !== 'undefined') {
                localStorage.setItem('authToken', token);
            }
        },
        clearAuth: () => {
            set({ authToken: null });
            if (typeof window !== 'undefined') {
                localStorage.removeItem('authToken');
            }
        }
    };
}

export const authStore = createAuthStore();
