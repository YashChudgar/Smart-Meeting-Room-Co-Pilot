import React, { useState } from 'react';
import RoomAvailability from './components/RoomAvailability';
import BookingForm from './components/BookingForm';

export default function App() {
  const [selectedRoom, setSelectedRoom] = useState(null);

  const handleSelectRoom = (room) => {
    setSelectedRoom(room);
    window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });
  };

  return (
    <div className="min-h-screen bg-gray-50 text-gray-900">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 py-4 px-6 shadow-sm">
        <div className="max-w-5xl mx-auto flex justify-between items-center">
          <h1 className="text-xl font-bold text-blue-600">Smart Meeting Room Co-Pilot</h1>
          <span className="text-xs bg-blue-100 text-blue-800 font-medium px-2.5 py-1 rounded">Sprint 1</span>
        </div>
      </header>

      {/* Main Content Layout */}
      <main className="max-w-5xl mx-auto p-6 space-y-8">
        <RoomAvailability onSelectRoom={handleSelectRoom} />
        
        <hr className="border-gray-200" />

        <BookingForm selectedRoom={selectedRoom} />
      </main>
    </div>
  );
}