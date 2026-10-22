const API_BASE_URL = 'http://localhost:8080/api';

// Mock data fallback if backend endpoints are still pending
const MOCK_ROOMS = [
  { id: 1, name: 'Room A', capacity: 6, status: 'Available' },
  { id: 2, name: 'Room B', capacity: 12, status: 'Occupied' },
  { id: 3, name: 'Room C', capacity: 4, status: 'Available' },
  { id: 4, name: 'Executive Boardroom', capacity: 20, status: 'Available' },
];

export const fetchRooms = async () => {
  try {
    const response = await fetch(`${API_BASE_URL}/rooms`);
    if (!response.ok) throw new Error('Backend rooms endpoint unavailable');
    return await response.json();
  } catch (error) {
    console.warn('Using mock room data:', error.message);
    return MOCK_ROOMS;
  }
};

export const createBooking = async (bookingData) => {
  try {
    const response = await fetch(`${API_BASE_URL}/bookings`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(bookingData),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || 'Failed to submit booking');
    }
    return await response.json();
  } catch (error) {
    // If backend isn't ready, simulate a successful mock response for testing frontend flow
    console.warn('Backend booking endpoint unavailable. Simulating success response.');
    await new Promise((resolve) => setTimeout(resolve, 800)); // simulate network delay
    return { success: true, message: 'Mock booking saved successfully!' };
  }
};