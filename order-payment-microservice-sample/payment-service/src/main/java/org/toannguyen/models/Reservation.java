package org.toannguyen.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@ToString
public class Reservation {
    @NonNull
    private int amountAvailable;
    private int amountReserved;
}
