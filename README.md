## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

IEEE 802.3 Ethernet frame format


    |SFD: Start frame delimiter flag ( 10101011 ) |
    ------------------------------------------------------------
    | Preamble | SFD | Destination address | Source address | Length, type | Data + Padding | CRC
    |  7bytes  |1byte|       6 bytes       |     6 bytes    |    2 bytes   |                | 4 bytes

    |maximum and minimum lengths|
    ----------------------------------------------------------------------------
    | Destination address | Source address | length PDU |     Data and Padding     |    CRC    |
    |       6 bytes       |     6 bytes    |   2 bytes  |                          |  4 bytes  |

                                                        |---minimum.len: 46 bytes--|
                                                        |--maximum.len: 1500 bytes-|

    |--------------------------- Minimum frame length: 64 bytes -----------------------|
    |-------------------------- Maximum frame length: 1518 bytes ----------------------|


Address : 6 bytes
    ex : 06 : 01 : 02 : 01 : 2C : 4B


*Sender:*
    *Send () -> 
        ** invoked every timeslot once
        ** conditions
            * do nothing 
            * retransmit the previous dataframe due to timeout
            * send new data frame

        ** consider current network time measure in time slots

    *Recv_Ack () ->
        ** invoked when ack is received
        ** time stamp when ack is received is considered
        ** calls timout function

    *Timeout () -> 
        ** called by the Ack method
        ** computes the most recent data packet's round trip time
        ** recompute the value of timeout

*Reciever:* 
    *Recv () -> 
        ** invoked after receiving data frame from the sender
    
    *Send_Ack () -> 
        ** builds the ACK and transmit

*Sliding window:*
    ** maximum number of unacknowledged frames cannot exceed window size
    ** 

    # Performance metrics 
    Receiver Throughput [ packets per slot ]
    RTT
    bandwidth-delay product
    utilization percentage
