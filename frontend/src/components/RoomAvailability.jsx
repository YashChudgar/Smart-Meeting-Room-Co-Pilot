import React, { useState, useEffect } from 'react';
import { fetchRooms } from '../services/api';

export default function RoomAvailability({ onSelectRoom }) {
  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchRooms()
      .then((data) => setRooms(data))
      .catch((err) => console.error(err))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return <div className="text-center py-6 text-gray-500">Loading available rooms...</div>;
  }

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <h2 className="text-xl font-bold text-gray-800">Meeting Room Availability</h2>
        <span className="text-sm text-gray-500">{rooms.length} Rooms Total</span>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {rooms.map((room) => {
          const isAvailable = room.status.toLowerCase() === 'available';
          return (
            <div
              key={room.id}
              className="bg-white border border-gray-200 rounded-lg p-5 shadow-sm hover:shadow-md transition flex flex-col justify-between"
            >
              <div>
                <div className="flex justify-between items-start mb-2">
                  <h3 className="text-lg font-semibold text-gray-800">{room.name}</h3>
                  <span
                    className={`px-2.5 py-1 text-xs font-medium rounded-full ${
                      isAvailable
                        ? 'bg-green-100 text-green-800'
                        : 'bg-red-100 text-red-800'
                    }`}
                  >
                    {room.status}
                  </span>
                </div>
                <p className="text-sm text-gray-600 mb-4">Capacity: {room.capacity} attendees</p>
              </div>

              {isAvailable && (
                <button
                  onClick={() => onSelectRoom && onSelectRoom(room)}
                  className="w-full bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium py-2 px-4 rounded transition"
                >
                  Book This Room
                </button>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
}