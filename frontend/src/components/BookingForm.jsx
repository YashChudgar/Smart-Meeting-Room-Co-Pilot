import React, { useState, useEffect } from 'react';
import { createBooking } from '../services/api';

export default function BookingForm({ selectedRoom, rooms = [], onBookingSuccess }) {
  const [roomId, setRoomId] = useState(selectedRoom ? selectedRoom.id : '');
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');
  const [attendees, setAttendees] = useState(1);
  
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [submitting, setSubmitting] = useState(false);

  // Update room selection if user clicks "Book This Room" from the availability card
  useEffect(() => {
    if (selectedRoom) {
      setRoomId(selectedRoom.id);
    }
  }, [selectedRoom]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccessMessage('');

    // Frontend Validations
    if (!roomId || !startTime || !endTime || !attendees) {
      setError('Please fill in all required fields.');
      return;
    }

    if (new Date(startTime) >= new Date(endTime)) {
      setError('End time must be strictly after the start time.');
      return;
    }

    if (Number(attendees) <= 0) {
      setError('Attendee count must be greater than zero.');
      return;
    }

    setSubmitting(true);

    try {
      const payload = {
        roomId: Number(roomId),
        startTime,
        endTime,
        attendeeCount: Number(attendees),
      };

      await createBooking(payload);
      setSuccessMessage('Booking submitted successfully!');
      
      // Clear form
      setStartTime('');
      setEndTime('');
      setAttendees(1);
      
      if (onBookingSuccess) onBookingSuccess();
    } catch (err) {
      setError(err.message || 'Failed to process booking. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="bg-white border border-gray-200 rounded-lg p-6 shadow-sm">
      <h2 className="text-xl font-bold text-gray-800 mb-4">New Room Booking</h2>

      {error && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 text-sm rounded">
          {error}
        </div>
      )}

      {successMessage && (
        <div className="mb-4 p-3 bg-green-50 border border-green-200 text-green-700 text-sm rounded">
          {successMessage}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Select Room</label>
          <select
            value={roomId}
            onChange={(e) => setRoomId(e.target.value)}
            required
            className="w-full border border-gray-300 rounded-md p-2 text-sm focus:ring-2 focus:ring-blue-500 focus:outline-none"
          >
            <option value="">-- Choose a Meeting Room --</option>
            <option value="1">Room A (Capacity: 6)</option>
            <option value="2">Room B (Capacity: 12)</option>
            <option value="3">Room C (Capacity: 4)</option>
            <option value="4">Executive Boardroom (Capacity: 20)</option>
          </select>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Start Date & Time</label>
            <input
              type="datetime-local"
              value={startTime}
              onChange={(e) => setStartTime(e.target.value)}
              required
              className="w-full border border-gray-300 rounded-md p-2 text-sm focus:ring-2 focus:ring-blue-500 focus:outline-none"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">End Date & Time</label>
            <input
              type="datetime-local"
              value={endTime}
              onChange={(e) => setEndTime(e.target.value)}
              required
              className="w-full border border-gray-300 rounded-md p-2 text-sm focus:ring-2 focus:ring-blue-500 focus:outline-none"
            />
          </div>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Attendee Count</label>
          <input
            type="number"
            min="1"
            value={attendees}
            onChange={(e) => setAttendees(e.target.value)}
            required
            className="w-full border border-gray-300 rounded-md p-2 text-sm focus:ring-2 focus:ring-blue-500 focus:outline-none"
          />
        </div>

        <button
          type="submit"
          disabled={submitting}
          className="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-md transition disabled:bg-blue-300"
        >
          {submitting ? 'Submitting Booking...' : 'Confirm Booking'}
        </button>
      </form>
    </div>
  );
}