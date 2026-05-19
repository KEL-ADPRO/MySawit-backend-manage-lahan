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
import com.mysawit.mysawit_kebun.grpc.Kebun;
import com.mysawit.mysawit_kebun.grpc.KebunServiceGrpc;
import com.mysawit.mysawit_kebun.grpc.Koordinat;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@GrpcService
@RequiredArgsConstructor
public class KebunGrpcEndpoint extends KebunServiceGrpc.KebunServiceImplBase {
    private final KebunService kebunService;

    private GetKebunByIdResponse toGetKebunByIdResponse(com.mysawit.mysawit_kebun.model.Kebun kebun) {
        return GetKebunByIdResponse.newBuilder().setKebun(toProtoKebun(kebun)).build();
    }

    private GetKebunByNameResponse toGetKebunByNameResponse(com.mysawit.mysawit_kebun.model.Kebun kebun) {
        return GetKebunByNameResponse.newBuilder().setKebun(toProtoKebun(kebun)).build();
    }

    private Kebun toProtoKebun(com.mysawit.mysawit_kebun.model.Kebun kebun) {
        if (kebun == null) {
            return Kebun.newBuilder()
                    .setId("")
                    .setNama("")
                    .setLuas(0.0)
                    .build();
        }

        Kebun.Builder builder = Kebun.newBuilder()
                .setId(kebun.getId().toString())
                .setNama(kebun.getNama())
                .setLuas(kebun.getLuas());

        if (kebun.getArea() != null) {
            builder.setArea(toProtoArea(kebun.getArea()));
        }

        if (kebun.getMandorId() != null) {
            builder.setMandorId(kebun.getMandorId());
        }

        if (!kebun.getSupirIds().isEmpty()) {
            builder.addAllSupirIds(kebun.getSupirIds());
        }
        return builder.build();
    }

    private com.mysawit.mysawit_kebun.grpc.Area toProtoArea(com.mysawit.mysawit_kebun.model.Area area) {
        com.mysawit.mysawit_kebun.grpc.Area.Builder builder = com.mysawit.mysawit_kebun.grpc.Area.newBuilder();
        if (area == null) {
            return builder.build();
        }
        builder.setBottomLeft(toProtoKoordinat(area.getBottomLeft()))
               .setBottomRight(toProtoKoordinat(area.getBottomRight()))
               .setTopRight(toProtoKoordinat(area.getTopRight()))
               .setTopLeft(toProtoKoordinat(area.getTopLeft()));
        return builder.build();
    }

    private Koordinat toProtoKoordinat(com.mysawit.mysawit_kebun.model.Koordinat koordinat) {
        return Koordinat.newBuilder()
                .setX(koordinat.getX())
                .setY(koordinat.getY())
                .build();
    }

    @Override
    public void getAllKebun(GetAllKebunRequest request, StreamObserver<GetAllKebunResponse> responseObserver) {
        try {
            List<com.mysawit.mysawit_kebun.model.Kebun> kebuns = kebunService.findAllKebun();
            GetAllKebunResponse.Builder response = GetAllKebunResponse.newBuilder();
            for (com.mysawit.mysawit_kebun.model.Kebun kebun : kebuns == null ? Collections.<com.mysawit.mysawit_kebun.model.Kebun>emptyList() : kebuns) {
                if (kebun != null) {
                    response.addKebuns(toProtoKebun(kebun));
                }
            }
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL.withDescription("Failed to fetch kebun data.").withCause(ex).asRuntimeException());
        }
    }

    @Override
    public void getKebunById(GetKebunByIdRequest request, StreamObserver<GetKebunByIdResponse> responseObserver) {
        try {
            com.mysawit.mysawit_kebun.model.Kebun kebun = kebunService.findById(request.getId());
            if (kebun == null) {
                responseObserver.onError(Status.NOT_FOUND.withDescription("Kebun with ID " + request.getId() + " not found.").asRuntimeException());
                return;
            }
            responseObserver.onNext(toGetKebunByIdResponse(kebun));
            responseObserver.onCompleted();
        } catch (IllegalArgumentException ex) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(ex.getMessage()).asRuntimeException());
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL.withDescription("Failed to fetch kebun by ID.").withCause(ex).asRuntimeException());
        }
    }

    @Override
    public void getKebunByName(GetKebunByNameRequest request, StreamObserver<GetKebunByNameResponse> responseObserver) {
        try {
            com.mysawit.mysawit_kebun.model.Kebun kebun = kebunService.findByName(request.getName());
            if (kebun == null) {
                responseObserver.onError(Status.NOT_FOUND.withDescription("Kebun with name " + request.getName() + " not found.").asRuntimeException());
                return;
            }
            responseObserver.onNext(toGetKebunByNameResponse(kebun));
            responseObserver.onCompleted();
        } catch (IllegalArgumentException ex) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(ex.getMessage()).asRuntimeException());
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL.withDescription("Failed to fetch kebun by name.").withCause(ex).asRuntimeException());
        }
    }

    @Override
    public void checkMandorAssignment(CheckMandorAssignmentRequest request, StreamObserver<CheckMandorAssignmentResponse> responseObserver) {
        try {
            Optional<com.mysawit.mysawit_kebun.model.Kebun> kebunOptional = Optional.ofNullable(kebunService.checkMandorAssignment(request.getMandorId()))
                    .orElse(Optional.empty());
            kebunOptional.ifPresentOrElse(
                    kebun -> responseObserver.onNext(CheckMandorAssignmentResponse.newBuilder()
                            .setAssigned(true)
                            .setKebunId(kebun.getId() == null ? "" : kebun.getId().toString())
                            .setNamaKebun(kebun.getNama() == null ? "" : kebun.getNama())
                            .setMessage("Mandor assignment found")
                            .build()),
                    () -> responseObserver.onNext(CheckMandorAssignmentResponse.newBuilder()
                            .setAssigned(false)
                            .setKebunId("")
                            .setNamaKebun("")
                            .setMessage("Mandor is not assigned to any kebun")
                            .build()));
            responseObserver.onCompleted();
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL.withDescription("Failed to check mandor assignment.").withCause(ex).asRuntimeException());
        }
    }

    @Override
    public void checkSupirAssignment(CheckSupirAssignmentRequest request, StreamObserver<CheckSupirAssignmentResponse> responseObserver) {
        try {
            Optional<com.mysawit.mysawit_kebun.model.Kebun> kebunOptional = Optional.ofNullable(kebunService.checkSupirAssignment(request.getSupirId()))
                    .orElse(Optional.empty());
            kebunOptional.ifPresentOrElse(
                    kebun -> responseObserver.onNext(CheckSupirAssignmentResponse.newBuilder()
                            .setAssigned(true)
                            .setKebunId(kebun.getId() == null ? "" : kebun.getId().toString())
                            .setNamaKebun(kebun.getNama() == null ? "" : kebun.getNama())
                            .setMessage("Supir assignment found")
                            .build()),
                    () -> responseObserver.onNext(CheckSupirAssignmentResponse.newBuilder()
                            .setAssigned(false)
                            .setKebunId("")
                            .setNamaKebun("")
                            .setMessage("Supir Truk is not assigned to any kebun")
                            .build()));
            responseObserver.onCompleted();
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL.withDescription("Failed to check supir assignment.").withCause(ex).asRuntimeException());
        }
    }
}
