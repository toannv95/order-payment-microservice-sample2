package org.toannguyen.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Reservation {
    @NonNull
    private int itemsAvailable;
    private int itemsReserved;
}
