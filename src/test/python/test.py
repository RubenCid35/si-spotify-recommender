import unittest
import socket
import json
import threading


class SocketServerTestCase(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        # Start the socket server in a separate thread
        cls.server_thread = threading.Thread(target=start_server)
        cls.server_thread.start()

    @classmethod
    def tearDownClass(cls):
        # Stop the socket server
        stop_server()

    def setUp(self):
        # Create a client socket and connect to the server
        self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.client_socket.connect(('localhost', 8888))

    def tearDown(self):
        # Close the client socket
        self.client_socket.close()

    def test_send_and_receive_json(self):
        # Send a JSON message to the server
        message = {'name': 'John', 'age': 25}
        self.client_socket.send(json.dumps(message).encode())

        # Receive the response from the server
        response = self.client_socket.recv(1024)
        response = json.loads(response.decode())

        # Verify the response
        self.assertEqual(response['status'], 'success')
        self.assertEqual(response['message'], 'Data received successfully')

        # Verify the data received by the server
        self.assertEqual(response['data'], message)


def start_server():
    # Create a server socket and start listening for connections
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind(('localhost', 8888))
    server_socket.listen(1)

    while True:
        # Accept client connections
        client_socket, _ = server_socket.accept()

        # Receive the JSON message from the client
        data = client_socket.recv(1024)
        data = json.loads(data.decode())

        # Process the received message
        # For example, send a success response back to the client
        response = {'status': 'success', 'message': 'Data received successfully', 'data': data}
        client_socket.send(json.dumps(response).encode())

        # Close the client socket
        client_socket.close()


def stop_server():
    # Stop the server socket
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.connect(('localhost', 8888))
    server_socket.close()


if __name__ == '__main__':
    unittest.main()
