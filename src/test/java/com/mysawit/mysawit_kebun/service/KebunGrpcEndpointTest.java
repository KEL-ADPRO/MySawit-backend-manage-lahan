package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.grpc.CheckMandorAssignmentRequest;
import com.mysawit.mysawit_kebun.grpc.CheckMandorAssignmentResponse;
import com.mysawit.mysawit_kebun.grpc.CheckSupirAssignmentRequest;
import com.mysawit.mysawit_kebun.grpc.CheckSupirAssignmentResponse;
import com.mysawit.mysawit_kebun.grpc.GetAllKebunRequest;
import com.mysawit.mysawit_kebun.grpc.GetAllKebunResponse;
import com.mysawit.mysawit_kebun.grpc.GetKebunByIdRequest;
import com.mysawit.mysawit_kebun.grpc.GetKebunByIdResponse;
import com.mysawit.mysawit_kebun.grpc.GetKebunByNameRequest;
import com.mysawit.mysawit_kebun.grpc.GetKebunByNameResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KebunGrpcEndpointTest {

    @Mock
    private KebunService kebunService;

    @InjectMocks
    private KebunGrpcEndpoint endpoint;

    private com.mysawit.mysawit_kebun.model.Kebun kebun1;
    private com.mysawit.mysawit_kebun.model.Kebun kebun2;

    @BeforeEach
    void setUp() {
        kebun1 = new com.mysawit.mysawit_kebun.model.Kebun();
        kebun1.setId(UUID.fromString("aa558a9a-1a39-460a-8860-71aa6aa63aa6"));
        kebun1.setNama("Kebun 1");
        kebun1.setLuas(100.0);
        kebun1.setArea(new com.mysawit.mysawit_kebun.model.Area(
                new com.mysawit.mysawit_kebun.model.Koordinat(0, 0),
                new com.mysawit.mysawit_kebun.model.Koordinat(10, 0),
                new com.mysawit.mysawit_kebun.model.Koordinat(10, 10),
                new com.mysawit.mysawit_kebun.model.Koordinat(0, 10)
        ));
        kebun1.setMandorId("mandor-123");

        kebun2 = new com.mysawit.mysawit_kebun.model.Kebun();
        kebun2.setId(UUID.fromString("bb558b9b-1b39-460b-8860-71bb6bb63bb6"));
        kebun2.setNama("Kebun 2");
        kebun2.setLuas(50.0);
        kebun2.setArea(new com.mysawit.mysawit_kebun.model.Area(
                new com.mysawit.mysawit_kebun.model.Koordinat(20, 20),
                new com.mysawit.mysawit_kebun.model.Koordinat(30, 20),
                new com.mysawit.mysawit_kebun.model.Koordinat(30, 30),
                new com.mysawit.mysawit_kebun.model.Koordinat(20, 30)
        ));
        kebun2.setSupirIds(new ArrayList<>(List.of("supir-001", "supir-002")));
    }

    private static final class RecordingObserver<T> implements StreamObserver<T> {
        private T value;
        private Throwable error;
        private boolean completed;

        @Override
        public void onNext(T value) {
            this.value = value;
        }

        @Override
        public void onError(Throwable throwable) {
            this.error = throwable;
        }

        @Override
        public void onCompleted() {
            this.completed = true;
        }
    }

    @Test
    void getAllKebunSuccess() {
        List<com.mysawit.mysawit_kebun.model.Kebun> kebunList = List.of(kebun1, kebun2);
        when(kebunService.findAllKebun()).thenReturn(kebunList);

        RecordingObserver<GetAllKebunResponse> observer = new RecordingObserver<>();
        endpoint.getAllKebun(GetAllKebunRequest.newBuilder().build(), observer);

        assertTrue(observer.completed);
        assertNull(observer.error);
        assertNotNull(observer.value);
        assertEquals(2, observer.value.getKebunsList().size());
        assertEquals("Kebun 1", observer.value.getKebunsList().get(0).getNama());
        assertEquals("Kebun 2", observer.value.getKebunsList().get(1).getNama());
    }

    @Test
    void getAllKebunReturnsEmptyList() {
        when(kebunService.findAllKebun()).thenReturn(new ArrayList<>());

        RecordingObserver<GetAllKebunResponse> observer = new RecordingObserver<>();
        endpoint.getAllKebun(GetAllKebunRequest.newBuilder().build(), observer);

        assertTrue(observer.completed);
        assertNull(observer.error);
        assertNotNull(observer.value);
        assertEquals(0, observer.value.getKebunsList().size());
    }

    @Test
    void getAllKebunHandlesNullList() {
        when(kebunService.findAllKebun()).thenReturn(null);

        RecordingObserver<GetAllKebunResponse> observer = new RecordingObserver<>();
        endpoint.getAllKebun(GetAllKebunRequest.newBuilder().build(), observer);

        assertTrue(observer.completed);
        assertNull(observer.error);
        assertNotNull(observer.value);
        assertEquals(0, observer.value.getKebunsList().size());
    }

    @Test
    void getAllKebunSkipsNullEntries() {
        List<com.mysawit.mysawit_kebun.model.Kebun> kebunList = new ArrayList<>();
        kebunList.add(kebun1);
        kebunList.add(null);
        kebunList.add(kebun2);
        when(kebunService.findAllKebun()).thenReturn(kebunList);

        RecordingObserver<GetAllKebunResponse> observer = new RecordingObserver<>();
        endpoint.getAllKebun(GetAllKebunRequest.newBuilder().build(), observer);

        assertTrue(observer.completed);
        assertNull(observer.error);
        assertNotNull(observer.value);
        assertEquals(2, observer.value.getKebunsList().size());
        assertEquals("Kebun 1", observer.value.getKebunsList().get(0).getNama());
        assertEquals("Kebun 2", observer.value.getKebunsList().get(1).getNama());
    }

    @Test
    void getKebunByIdSuccess() {
        when(kebunService.findById("aa558a9a-1a39-460a-8860-71aa6aa63aa6")).thenReturn(kebun1);

        RecordingObserver<GetKebunByIdResponse> observer = new RecordingObserver<>();
        endpoint.getKebunById(
                GetKebunByIdRequest.newBuilder().setId("aa558a9a-1a39-460a-8860-71aa6aa63aa6").build(),
                observer
        );

        assertTrue(observer.completed);
        assertNull(observer.error);
        assertNotNull(observer.value);
        assertEquals("Kebun 1", observer.value.getKebun().getNama());
        assertEquals(100.0, observer.value.getKebun().getLuas(), 0.01);
        assertEquals("mandor-123", observer.value.getKebun().getMandorId());
    }

    @Test
    void getKebunByIdNotFound() {
        when(kebunService.findById("invalid-id")).thenThrow(
                new IllegalArgumentException("Kebun with ID invalid-id not found.")
        );

        RecordingObserver<GetKebunByIdResponse> observer = new RecordingObserver<>();
        endpoint.getKebunById(
                GetKebunByIdRequest.newBuilder().setId("invalid-id").build(),
                observer
        );

        assertFalse(observer.completed);
        assertNotNull(observer.error);
        assertEquals(Status.NOT_FOUND.getCode(), Status.fromThrowable(observer.error).getCode());
        assertEquals("Kebun with ID invalid-id not found.", Status.fromThrowable(observer.error).getDescription());
    }

    @Test
    void getKebunByIdNullResultReturnsNotFound() {
        when(kebunService.findById("missing-id")).thenReturn(null);

        RecordingObserver<GetKebunByIdResponse> observer = new RecordingObserver<>();
        endpoint.getKebunById(
                GetKebunByIdRequest.newBuilder().setId("missing-id").build(),
                observer
        );

        assertFalse(observer.completed);
        assertNotNull(observer.error);
        assertEquals(Status.NOT_FOUND.getCode(), Status.fromThrowable(observer.error).getCode());
        assertEquals("Kebun with ID missing-id not found.", Status.fromThrowable(observer.error).getDescription());
    }

    @Test
    void getKebunByNameSuccess() {
        when(kebunService.findByName("Kebun 1")).thenReturn(kebun1);

        RecordingObserver<GetKebunByNameResponse> observer = new RecordingObserver<>();
        endpoint.getKebunByName(
                GetKebunByNameRequest.newBuilder().setName("Kebun 1").build(),
                observer
        );

        assertTrue(observer.completed);
        assertNull(observer.error);
        assertNotNull(observer.value);
        assertEquals("Kebun 1", observer.value.getKebun().getNama());
        assertEquals("aa558a9a-1a39-460a-8860-71aa6aa63aa6", observer.value.getKebun().getId());
    }

    @Test
    void getKebunByNameNotFound() {
        when(kebunService.findByName("Kebun Tidak Ada")).thenThrow(
                new IllegalArgumentException("Kebun with name Kebun Tidak Ada not found.")
        );

        RecordingObserver<GetKebunByNameResponse> observer = new RecordingObserver<>();
        endpoint.getKebunByName(
                GetKebunByNameRequest.newBuilder().setName("Kebun Tidak Ada").build(),
                observer
        );

        assertFalse(observer.completed);
        assertNotNull(observer.error);
        assertEquals(Status.NOT_FOUND.getCode(), Status.fromThrowable(observer.error).getCode());
        assertEquals("Kebun with name Kebun Tidak Ada not found.", Status.fromThrowable(observer.error).getDescription());
    }

    @Test
    void getKebunByNameNullResultReturnsNotFound() {
        when(kebunService.findByName("Missing Kebun")).thenReturn(null);

        RecordingObserver<GetKebunByNameResponse> observer = new RecordingObserver<>();
        endpoint.getKebunByName(
                GetKebunByNameRequest.newBuilder().setName("Missing Kebun").build(),
                observer
        );

        assertFalse(observer.completed);
        assertNotNull(observer.error);
        assertEquals(Status.NOT_FOUND.getCode(), Status.fromThrowable(observer.error).getCode());
        assertEquals("Kebun with name Missing Kebun not found.", Status.fromThrowable(observer.error).getDescription());
    }

    @Test
    void checkMandorAssignmentSuccess() {
        when(kebunService.checkMandorAssignment("mandor-123")).thenReturn(Optional.of(kebun1));

        RecordingObserver<CheckMandorAssignmentResponse> observer = new RecordingObserver<>();
        endpoint.checkMandorAssignment(
                CheckMandorAssignmentRequest.newBuilder().setMandorId("mandor-123").build(),
                observer
        );

        assertTrue(observer.completed);
        assertNull(observer.error);
        assertNotNull(observer.value);
        assertTrue(observer.value.getAssigned());
        assertEquals("aa558a9a-1a39-460a-8860-71aa6aa63aa6", observer.value.getKebunId());
        assertEquals("Kebun 1", observer.value.getNamaKebun());
        assertEquals("Mandor assignment found", observer.value.getMessage());
    }

    @Test
    void checkMandorAssignmentNotAssigned() {
        when(kebunService.checkMandorAssignment("mandor-999")).thenReturn(Optional.empty());

        RecordingObserver<CheckMandorAssignmentResponse> observer = new RecordingObserver<>();
        endpoint.checkMandorAssignment(
                CheckMandorAssignmentRequest.newBuilder().setMandorId("mandor-999").build(),
                observer
        );

        assertTrue(observer.completed);
        assertNull(observer.error);
        assertNotNull(observer.value);
        assertFalse(observer.value.getAssigned());
        assertEquals("", observer.value.getKebunId());
        assertEquals("", observer.value.getNamaKebun());
        assertEquals("Mandor is not assigned to any kebun", observer.value.getMessage());
    }

    @Test
    void checkMandorAssignmentNullOptionalHandledAsEmpty() {
        when(kebunService.checkMandorAssignment("mandor-null")).thenReturn(null);

        RecordingObserver<CheckMandorAssignmentResponse> observer = new RecordingObserver<>();
        endpoint.checkMandorAssignment(
                CheckMandorAssignmentRequest.newBuilder().setMandorId("mandor-null").build(),
                observer
        );

        assertTrue(observer.completed);
        assertNull(observer.error);
        assertNotNull(observer.value);
        assertFalse(observer.value.getAssigned());
        assertEquals("", observer.value.getKebunId());
        assertEquals("", observer.value.getNamaKebun());
    }

    @Test
    void checkSupirAssignmentSuccess() {
        when(kebunService.checkSupirAssignment("supir-001")).thenReturn(Optional.of(kebun2));

        RecordingObserver<CheckSupirAssignmentResponse> observer = new RecordingObserver<>();
        endpoint.checkSupirAssignment(
                CheckSupirAssignmentRequest.newBuilder().setSupirId("supir-001").build(),
                observer
        );

        assertTrue(observer.completed);
        assertNull(observer.error);
        assertNotNull(observer.value);
        assertTrue(observer.value.getAssigned());
        assertEquals("bb558b9b-1b39-460b-8860-71bb6bb63bb6", observer.value.getKebunId());
        assertEquals("Kebun 2", observer.value.getNamaKebun());
        assertEquals("Supir assignment found", observer.value.getMessage());
    }

    @Test
    void checkSupirAssignmentNotAssigned() {
        when(kebunService.checkSupirAssignment("supir-999")).thenReturn(Optional.empty());

        RecordingObserver<CheckSupirAssignmentResponse> observer = new RecordingObserver<>();
        endpoint.checkSupirAssignment(
                CheckSupirAssignmentRequest.newBuilder().setSupirId("supir-999").build(),
                observer
        );

        assertTrue(observer.completed);
        assertNull(observer.error);
        assertNotNull(observer.value);
        assertFalse(observer.value.getAssigned());
        assertEquals("", observer.value.getKebunId());
        assertEquals("", observer.value.getNamaKebun());
        assertEquals("Supir Truk is not assigned to any kebun", observer.value.getMessage());
    }

    @Test
    void checkSupirAssignmentNullOptionalHandledAsEmpty() {
        when(kebunService.checkSupirAssignment("supir-null")).thenReturn(null);

        RecordingObserver<CheckSupirAssignmentResponse> observer = new RecordingObserver<>();
        endpoint.checkSupirAssignment(
                CheckSupirAssignmentRequest.newBuilder().setSupirId("supir-null").build(),
                observer
        );

        assertTrue(observer.completed);
        assertNull(observer.error);
        assertNotNull(observer.value);
        assertFalse(observer.value.getAssigned());
        assertEquals("", observer.value.getKebunId());
        assertEquals("", observer.value.getNamaKebun());
    }

    @Test
    void checkSupirAssignmentWithMultipleSupirs() {
        when(kebunService.checkSupirAssignment("supir-002")).thenReturn(Optional.of(kebun2));

        RecordingObserver<CheckSupirAssignmentResponse> observer = new RecordingObserver<>();
        endpoint.checkSupirAssignment(
                CheckSupirAssignmentRequest.newBuilder().setSupirId("supir-002").build(),
                observer
        );

        assertTrue(observer.completed);
        assertNull(observer.error);
        assertNotNull(observer.value);
        assertTrue(observer.value.getAssigned());
        assertEquals("Supir assignment found", observer.value.getMessage());
    }

    @Test
    void protoKebunMapsAllFieldsCorrectly() {
        when(kebunService.findById("aa558a9a-1a39-460a-8860-71aa6aa63aa6")).thenReturn(kebun1);

        RecordingObserver<GetKebunByIdResponse> observer = new RecordingObserver<>();
        endpoint.getKebunById(
                GetKebunByIdRequest.newBuilder().setId("aa558a9a-1a39-460a-8860-71aa6aa63aa6").build(),
                observer
        );

        assertNotNull(observer.value);
        var protoKebun = observer.value.getKebun();
        assertEquals("aa558a9a-1a39-460a-8860-71aa6aa63aa6", protoKebun.getId());
        assertEquals("Kebun 1", protoKebun.getNama());
        assertEquals(100.0, protoKebun.getLuas(), 0.01);
        assertEquals("mandor-123", protoKebun.getMandorId());
        assertNotNull(protoKebun.getArea());
        assertEquals(0, protoKebun.getArea().getBottomLeft().getX());
        assertEquals(0, protoKebun.getArea().getBottomLeft().getY());
        assertEquals(10, protoKebun.getArea().getTopRight().getX());
        assertEquals(10, protoKebun.getArea().getTopRight().getY());
    }

    @Test
    void protoKebunWithSupirIds() {
        when(kebunService.findById("bb558b9b-1b39-460b-8860-71bb6bb63bb6")).thenReturn(kebun2);

        RecordingObserver<GetKebunByIdResponse> observer = new RecordingObserver<>();
        endpoint.getKebunById(
                GetKebunByIdRequest.newBuilder().setId("bb558b9b-1b39-460b-8860-71bb6bb63bb6").build(),
                observer
        );

        assertNotNull(observer.value);
        var protoKebun = observer.value.getKebun();
        assertEquals(2, protoKebun.getSupirIdsList().size());
        assertTrue(protoKebun.getSupirIdsList().contains("supir-001"));
        assertTrue(protoKebun.getSupirIdsList().contains("supir-002"));
    }
}

